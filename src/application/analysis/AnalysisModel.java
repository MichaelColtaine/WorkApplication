package application.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.shared.ArticleRow;
import application.shared.ExportArticle;

public class AnalysisModel {

	private AnalysisSettings settings;
	private HashMap<String, ArticleRow> analysis = new HashMap<>();
	private ArrayList<ExportArticle> orders;
	private ArrayList<ExportArticle> returns;

	public AnalysisModel() {
		this.settings = new AnalysisSettings();
	}

	private static class LazyHolder {
		static final AnalysisModel INSTANCE = new AnalysisModel();
	}

	public static AnalysisModel getInstance() {
		return LazyHolder.INSTANCE;
	}

	public AnalysisSettings getSettings() {
		return settings;
	}

	public void setAnalysis(HashMap<String, ArticleRow> analysis) {
		this.analysis = analysis;
	}

	public HashMap<String, ArticleRow> getAnalysis() {
		return analysis;
	}

	public void setReturns(ArrayList<ExportArticle> returns) {
		this.returns = returns;
	}

	public void setOrders(ArrayList<ExportArticle> orders) {
		this.orders = orders;
	}

	public ArrayList<ExportArticle> getOrders() {
		return this.orders;
	}

	public ArrayList<ExportArticle> getReturns() {
		return this.returns;
	}

}
