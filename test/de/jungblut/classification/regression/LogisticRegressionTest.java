package de.jungblut.classification.regression;

import junit.framework.TestCase;

import org.junit.Test;

import com.google.common.math.DoubleMath;

import de.jungblut.classification.Classifier;
import de.jungblut.classification.Evaluator;
import de.jungblut.classification.Evaluator.EvaluationResult;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;
import de.jungblut.math.minimize.Fmincg;
import de.jungblut.math.tuple.Tuple;
import de.jungblut.reader.CsvDatasetReader;

public class LogisticRegressionTest extends TestCase {

  private static DoubleVector[] features;
  private static DenseDoubleVector[] outcome;
  private static DenseDoubleVector y;
  private static DenseDoubleMatrix x;

  static {
    LogisticRegression.SEED = 0L;
    Tuple<DoubleVector[], DenseDoubleVector[]> readCsv = CsvDatasetReader
        .readCsv("files/logreg/ex2data1.txt", ',', null, 2, 2);
    features = readCsv.getFirst();
    outcome = readCsv.getSecond();
    double[] classes = new double[outcome.length];
    for (int i = 0; i < outcome.length; i++) {
      classes[i] = outcome[i].get(0);
    }
    y = new DenseDoubleVector(classes);
    x = new DenseDoubleMatrix(features);
  }

  @Test
  public void testLogisticRegression() {

    LogisticRegressionCostFunction fnc = new LogisticRegressionCostFunction(
        new DenseDoubleMatrix(features), y, 1d);

    DoubleVector theta = Fmincg.minimizeFunction(fnc, new DenseDoubleVector(
        new double[] { 0, 0, 0 }), 1000, false);

    assertEquals(-25.052165981708658, theta.get(0));
    assertEquals(0.20535460559228136, theta.get(1));
    assertEquals(0.20058370043792928, theta.get(2));
  }

  @Test
  public void testPredictions() {
    LogisticRegression reg = new LogisticRegression(1.0d, new Fmincg(), 1000,
        false);
    reg.train(x, y);
    DoubleVector predict = reg.predict(x, 0.5d);

    double wrongPredictions = predict.subtract(y).abs().sum();
    assertEquals(11.0d, wrongPredictions);
    double trainAccuracy = (y.getLength() - wrongPredictions) / y.getLength();

    assertTrue(trainAccuracy > 0.85);
  }

  @Test
  public void testRegressionInterface() {
    Classifier clf = new LogisticRegression(1.0d, new Fmincg(), 1000, false);
    clf.train(features, outcome);
    double trainingError = 0d;
    for (int i = 0; i < features.length; i++) {
      int predict = clf.getPredictedClass(features[i], 0.5d);
      trainingError += Math.abs(outcome[i].get(0) - predict);
    }
    assertTrue("Training error was: " + trainingError
        + " and should have been between 9 and 13.",
        DoubleMath.fuzzyEquals(trainingError, 11, 2d));
  }

  @Test
  public void testRegressionEvaluation() {
    Classifier clf = new LogisticRegression(1.0d, new Fmincg(), 1000, false);
    EvaluationResult eval = Evaluator.evaluateClassifier(clf, features,
        outcome, 2, 0.9f, false, 0.5d);
    assertEquals(1d, eval.getPrecision());
    assertEquals(9, eval.getTestSize());
    assertEquals(91, eval.getTrainSize());
  }
}
