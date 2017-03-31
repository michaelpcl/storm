package com.cdel.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import com.cdel.wordcount.bolt.SplitSentence;
import com.cdel.wordcount.bolt.WordCount;
import com.cdel.wordcount.spout.RandomSentenceSpout;

//http://www.aboutyun.com/thread-7394-1-1.html
//http://www.csdn.net/article/2012-12-24/2813117-storm-realtime-big-data-analysis
public class WordCountTopology {

	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = new TopologyBuilder();

		// 数据源
		builder.setSpout("spout", new RandomSentenceSpout(), 5);

		/**
		 * Storm的Grouping即消息的Partition机制。当一个Tuple被发送时，如何确定将它发送个某个（些）Task来处理？？
		 * ShuffleGrouping：随机选择一个Task来发送。
		 * FiledGrouping：根据Tuple中Fields来做一致性hash，相同hash值的Tuple被发送到相同的Task。
		 * AllGrouping：广播发送，将每一个Tuple发送到所有的Task。
		 * GlobalGrouping：所有的Tuple会被发送到某个Bolt中的id最小的那个Task。
		 * NoneGrouping：不关心Tuple发送给哪个Task来处理，等价于ShuffleGrouping。
		 * DirectGrouping：直接将Tuple发送到指定的Task来处理。
		 */
		// 上游是spout，随机分发shuffleGrouping
		builder.setBolt("split", new SplitSentence(), 8).shuffleGrouping("spout");
		// 上游是split，按Fields(word)进行分组，把相同的word字段发到同一个bolt
		builder.setBolt("count", new WordCount(), 12).fieldsGrouping("split", new Fields("word"));

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			//设置workers，启动多少个jvm进程
			conf.setNumWorkers(3);

			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			// 最大任务并发
			conf.setMaxTaskParallelism(3);

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("word-count", conf, builder.createTopology());

			Thread.sleep(10000);

			cluster.shutdown();
		}
	}
}
