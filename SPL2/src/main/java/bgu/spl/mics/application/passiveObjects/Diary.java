package bgu.spl.mics.application.passiveObjects;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	/**
	 * Retrieves the single instance of this class.
	 */
	private static Diary instance = new Diary();
	private  List<Report> reports = new LinkedList<>();
	private int total=0;

	public static Diary getInstance() {
		return instance;
	}
	public void increment(){
		++this.total;
	}
	public List<Report> getReports() {
		return null;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		//TODO: Implement this
		reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) throws IOException {
		//TODO: Implement this
		JsonObject toprint = new JsonObject();
		JsonArray reports = new JsonArray();
		for(Report report : this.reports) {
			JsonObject mission = new JsonObject();
			mission.addProperty("missionName", report.getMissionName());
			mission.addProperty("m", report.getM());
			mission.addProperty("moneypenny", report.getMoneypenny());
			JsonArray tmparray = new JsonArray();
			for(String serial : report.getAgentsSerialNumbersNumber()){
				tmparray.add(serial);
			}
			mission.add("agentsSerialNumbers", tmparray);

			tmparray = new JsonArray();

			for(String name : report.getAgentsNames()){
				tmparray.add(name);
			}
			mission.add("agentsNames", tmparray);
			mission.addProperty("gadgetName", report.getGadgetName());
			mission.addProperty("timeCreated", report.getTimeCreated());
			mission.addProperty("timeIssued", report.getTimeIssued());
			mission.addProperty("qTime", report.getQTime());

			reports.add(mission);

		}
		toprint.add("reports", reports);
		toprint.addProperty("total", total);

		FileWriter file = new FileWriter(filename);
		file.write(new GsonBuilder().setPrettyPrinting().create().toJson(toprint));
		file.close();

	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		//TODO: Implement this
		return reports.size();
	}
}
