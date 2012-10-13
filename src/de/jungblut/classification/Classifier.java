package de.jungblut.classification;

import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleVector;

/**
 * Classifier interface for predicting categorial variables.
 * 
 * @author thomas.jungblut
 * 
 */
public interface Classifier {

  /**
   * Trains this classifier with the given features and the outcome.
   * 
   * @param outcome the outcome must have classes labeled as doubles. E.G. in
   *          the binary case you have a single element and decide between 0d
   *          and 1d. In higher dimensional cases you have each of these single
   *          elements mapped to a dimension.
   */
  public void train(DoubleVector[] features, DenseDoubleVector[] outcome);

  /**
   * Classifies the given features.
   * 
   * @return the vector that contains a "probability" at the index of the class.
   */
  public DoubleVector predict(DoubleVector features);

  /**
   * Classifies the given features.
   * 
   * @return the predicted class as an integer for the output of a classifier.
   */
  public int getPredictedClass(DoubleVector features);

}
