package com.cdel.exclamation.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

/**
 * Spout是流的源头， 通常从外部数据源读取tuple， 并emit到topology中。
 * TestWordSpout从["nathan", "mike", "jackson", "golda", "bertels"]里面随机选择一个单词发射出来。
 *
 * Spout：在一个topology中产生源数据流的组件。通常情况下spout会从外部数据源中读取数据，然后转换为topology内部的源数据。
 * Spout是一个主动的角色，其接口中有个nextTuple()函数，storm框架会不停地调用此函数，用户只要在其中生成源数据即可。
 *
 */
@SuppressWarnings("serial")
public class TestWordSpout extends BaseRichSpout {
	SpoutOutputCollector _collector;
	Random _rand;

	@SuppressWarnings("rawtypes")
	public void open(Map map, TopologyContext topologycontext, SpoutOutputCollector spoutoutputcollector) {
		_collector = spoutoutputcollector;
	    _rand = new Random();
	}

	//nextTuple()函数， Storm 框架会不停地调用此函数，用户只要在其中生成源数据即可。
	//nextTuple()会发出一个新 Tuple 到拓扑，如果没有新的元组发出则简单地返回。 nextTuple()方法不阻止任何 Spout 的实现， 因为 Storm 在同一线程调用所有的 Spout 方法。
	public void nextTuple() {
		Utils.sleep(100);
	    final String[] words =new String[] {"nathan", "mike", "jackson", "golda", "bertels"};
	    final Random rand =new Random();
	    final String word = words[rand.nextInt(words.length)];
	    _collector.emit(new Values(word));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	//当 Storm 检测到一个元组从 Spout 发出时， ack()和fail()会被调用，要么成功完成通过拓扑，要么未能完成。 ack()和 fail()仅被可靠的 Spout 调用。
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}

}