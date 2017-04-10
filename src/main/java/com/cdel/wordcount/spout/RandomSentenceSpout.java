package com.cdel.wordcount.spout;

import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

@SuppressWarnings("serial")
public class RandomSentenceSpout extends BaseRichSpout {
	SpoutOutputCollector _collector;
	Random _rand;

	@SuppressWarnings("rawtypes")
	//初始化方法
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		_collector = collector;
		_rand = new Random();
	}

	//持续不断的调用nextTuple
	public void nextTuple() {
		Utils.sleep(100);
		String[] sentences = new String[] { "the cow jumped over the moon",
				"an apple a day keeps the doctor away",
				"four score and seven years ago",
				"snow white and the seven dwarfs", "i am at two with nature" };
		String sentence = sentences[_rand.nextInt(sentences.length)];
		_collector.emit(new Values(sentence));
		//带一个msgID
		//_collector.emit(new Values(sentence), 111);
	}

	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
		//如果有失败，会调用这个方法，参数是msgID
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//声明元组new Fields("word")，告诉框架输出哪些字段
		//流由元组组成，OutputFieldsDeclarer声明流
		declarer.declare(new Fields("word"));
	}

}