package spouts;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class WordGenerator extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private int sentenceSize = 140;
	Random rand;
	private char[] characters = {'a','b','c','d','e','f','g','h','i','j','k','l','m',' ',
				     			 'n','o','p','q','r','s','t','u','v','x','w','y','z',' ',
				     			 'A','B','C','D','E','F','G','H','I','J','K','L','M',' ',
				     			 'N','O','P','Q','R','S','T','U','V','X','W','Y','Z',' '};
	
	@Override
	public void ack(Object msgId) {
		System.out.println("OK:"+msgId);
	}
	
	@Override
	public void close() {}
	
	@Override
	public void fail(Object msgId) {
		System.out.println("FAIL:"+msgId);
	}

	/**
	 * The only thing that the methods will do It is emit each 
	 * file line
	 */
	@Override
	public void nextTuple() {
		Utils.sleep(500);
		/**
		 * The nextTuple it is called forever
		 */
		String sentence = "";
        
		for (int i = 0; i < sentenceSize; i++) {
			sentence = sentence + characters[rand.nextInt(characters.length)];
		}
		this.collector.emit(new Values(sentence));
	}

	/**
	 * We will create the file and get the collector object
	 */
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.rand = new Random();
		this.collector = collector;
	}

	/**
	 * Declare the output field "word"
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
}