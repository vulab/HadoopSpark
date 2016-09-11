package com.vulab;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.mllib.tree.impurity.Entropy;
import org.apache.spark.mllib.tree.impurity.Gini;
import org.apache.spark.mllib.tree.impurity.Impurity;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class SparkPredictiveAnalyticsVulabExample {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("SparkPredictiveAnalyticsVulabExample").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		SQLContext sqlContext = new SQLContext(sc);
		JavaRDD<InsuranceData> insuranceDataAll = sc.textFile("data/data.csv")
				.map(new Function<String, InsuranceData>() {
					public InsuranceData call(String line) throws Exception {
						String[] parts = line.split(",");
						InsuranceData idata = new InsuranceData();
						idata.setCreditability(Double.parseDouble(parts[0]));
						idata.setBalance(Double.parseDouble(parts[1]));
						idata.setDuration(Double.parseDouble(parts[2]));
						idata.setHistory(Double.parseDouble(parts[3]));
						idata.setPurpose(Double.parseDouble(parts[4]));
						idata.setAmount(Double.parseDouble(parts[5]));
						idata.setSavings(Double.parseDouble(parts[6]));
						idata.setEmployment(Double.parseDouble(parts[7]));
						idata.setInstPercent(Double.parseDouble(parts[8]));
						idata.setSexMarried(Double.parseDouble(parts[9]));
						idata.setGuarantors(Double.parseDouble(parts[10]));
						idata.setResidenceDuration(Double.parseDouble(parts[11]));
						idata.setAssets(Double.parseDouble(parts[12]));
						idata.setAge(Double.parseDouble(parts[13]));
						idata.setConcCredit(Double.parseDouble(parts[14]));
						idata.setApartment(Double.parseDouble(parts[15]));
						idata.setCredits(Double.parseDouble(parts[16]));
						idata.setOccupation(Double.parseDouble(parts[17]));
						idata.setDependents(Double.parseDouble(parts[18]));
						idata.setHasPhone(Double.parseDouble(parts[19]));
						idata.setForeign(Double.parseDouble(parts[20]));
						return idata;
					}
				});

		DataFrame schemaInsuranceData = sqlContext.createDataFrame(insuranceDataAll, InsuranceData.class);

		schemaInsuranceData.registerTempTable("insurancetable");
		schemaInsuranceData.printSchema();
		schemaInsuranceData.show(20);
		schemaInsuranceData.describe("balance").show();
		schemaInsuranceData.groupBy("creditability").avg("balance").show();

		sqlContext.sql("SELECT creditability, avg(balance) as avgbalance, avg(amount) as avgamt, avg(duration) as avgdur  FROM insurancetable GROUP BY creditability ").show();

		String[] featureCols = { "balance", "duration", "history", "purpose", "amount", "savings", "employment",
				"instPercent", "sexMarried", "guarantors", "residenceDuration", "assets", "age", "concCredit",
				"apartment", "credits", "occupation", "dependents", "hasPhone", "foreign" };

		VectorAssembler assembler = new VectorAssembler().setInputCols(featureCols).setOutputCol("features");
		DataFrame df2 = assembler.transform(schemaInsuranceData);
		df2.show();

		StringIndexer labelIndexer = new StringIndexer().setInputCol("creditability").setOutputCol("label");
		DataFrame df3 = labelIndexer.fit(df2).transform(df2);
		df3.show();

		long splitSeed = 5043;
		double[] weights = { 0.7, 0.3 };
		DataFrame[] dataFrameArray = df3.randomSplit(weights, splitSeed);

		DataFrame trainingData = dataFrameArray[0];
		DataFrame testData = dataFrameArray[1];

		RandomForestClassifier classifier = new RandomForestClassifier().setImpurity("gini").setMaxDepth(3)
				.setNumTrees(20).setFeatureSubsetStrategy("auto").setSeed(5043);
		RandomForestClassificationModel model = classifier.fit(trainingData);

		model.toDebugString();

		DataFrame predictions = model.transform(testData);

		BinaryClassificationEvaluator evaluator = new BinaryClassificationEvaluator().setLabelCol("label");
		double accuracy = evaluator.evaluate(predictions);
		System.out.println("ACCURACY is " + accuracy);
		// training using ML Pipeline
		int[] maxbinsArray = { 25, 28, 31 };
		int[] maxDepthArray = { 4, 6, 8 };
		String[] impurityList = { "Entropy", "Gini" };

		List<Impurity> imList = Arrays.asList(Entropy.instance(), Gini.instance());
		// Iterable<Impurity> iterable = Arrays.asList(impurityList);

		ParamMap[] paramGrid = new ParamGridBuilder().addGrid(classifier.maxBins(), maxbinsArray)
				.addGrid(classifier.maxDepth(), maxDepthArray).build();
		PipelineStage[] steps = { classifier };
		Pipeline pipeline = new Pipeline().setStages(steps);
		
		
		evaluator = new BinaryClassificationEvaluator() .setLabelCol("label");
		CrossValidator cv = new CrossValidator()
				  .setEstimator(pipeline)
				  .setEvaluator(evaluator)
				  .setEstimatorParamMaps(paramGrid)
				  .setNumFolds(10);
		
		CrossValidatorModel pipelineFittedModel = cv.fit(trainingData);
		
		DataFrame predictionsAgain = pipelineFittedModel.transform(testData);
				double accuracyAgain = evaluator.evaluate(predictionsAgain);
				System.out.println("ACCURACY is " + accuracy);
				System.out.println("accuracyAgain is " + accuracyAgain);
	}
}
