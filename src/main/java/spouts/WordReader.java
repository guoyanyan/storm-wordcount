package spouts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class WordReader extends BaseRichSpout {

	private SpoutOutputCollector collector;
	private FileReader fileReader;
	
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
		 * The nextTuple it is called forever, so if we have been read the file
		 * we will wait and then return
		 */
		System.out.println("Entry file path: ");
		InputStreamReader filePath = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(filePath);
		
		try {
			this.fileReader = new FileReader(in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String str;
		//Open the reader
		BufferedReader reader = new BufferedReader(fileReader);
		try {
			//Read all lines
			while ((str = reader.readLine()) != null) {
				/**
				 * By each line emit a new value with the line as a their
				 */
				this.collector.emit(new Values(str));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading tuple",e);
		}
	}

	/**
	 * We will create the file and get the collector object
	 */
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
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
