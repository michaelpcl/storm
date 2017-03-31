package com.cdel.wordcount.bolt;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

@SuppressWarnings("serial")
public class WordCount extends BaseBasicBolt {

	Map<String, Integer> counts = new HashMap<String, Integer>();

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String word = tuple.getString(0);
		Integer count = counts.get(word);
		if (count == null)
			count = 0;
		count++;
		counts.put(word, count);
		collector.emit(new Values(word, count));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

	@Override
	//最后输出，但在集群里不能保证cleanup被调用
	public void cleanup(){
		for (String key : counts.keySet()) {
			System.out.println(key + " : " + counts.get(key));
		}
	}

}
