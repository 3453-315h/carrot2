/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2019, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * https://www.carrot2.org/carrot2.LICENSE
 */
package org.carrot2.examples;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.carrot2.clustering.Cluster;
import org.carrot2.clustering.ClusteringAlgorithm;
import org.carrot2.clustering.Document;
import org.carrot2.clustering.kmeans.BisectingKMeansClusteringAlgorithm;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import org.carrot2.language.LanguageComponents;
import org.junit.Test;

/** This example shows typical basic clustering scenarios. */
public class E01_ClusteringBasics {
  @Test
  public void clusterDocumentStream() throws IOException {
    // fragment-start{setup-heavy-components}
    // Our documents are in English so we load appropriate language resources.
    // This call can be heavy and an instance of LanguageComponents should be
    // reused across different clustering calls.
    LanguageComponents languageComponents = LanguageComponents.load("English");
    // fragment-end{setup-heavy-components}

    // fragment-start{setup-lightweight-components}
    LingoClusteringAlgorithm algorithm = new LingoClusteringAlgorithm();
    // fragment-end{setup-lightweight-components}

    // fragment-start{clustering-document-stream}
    // Create a stream of "documents" for clustering.
    // Each such document provides text content fields to a visitor.
    Stream<Document> documentStream =
        Arrays.stream(ExamplesData.DOCUMENTS_DATA_MINING)
            .map(
                fields ->
                    (fieldVisitor) -> {
                      fieldVisitor.accept("title", fields[1]);
                      fieldVisitor.accept("content", fields[2]);
                    });
    // fragment-end{clustering-document-stream}

    // fragment-start{clustering}
    // Perform clustering.
    List<Cluster<Document>> clusters;
    clusters = algorithm.cluster(documentStream, languageComponents);

    // Print cluster labels and a document count in each top-level cluster.
    for (Cluster<Document> c : clusters) {
      String label = String.join("; ", c.getLabels());
      System.out.println(label + ", documents: " + c.getDocuments().size());
    }
    // fragment-end{clustering}
  }

  @Test
  public void clusterWithQueryHint() throws IOException {
    LanguageComponents languageComponents = LanguageComponents.load("English");

    Stream<Document> documentStream = ExamplesData.documentStream();

    // Perform clustering again, this time provide a "hint" about terms that should be penalized.
    // Typically these are search query terms that would form trivial clusters.
    LingoClusteringAlgorithm algorithm = new LingoClusteringAlgorithm();
    algorithm.queryHint.set("data mining");
    List<Cluster<Document>> clusters = algorithm.cluster(documentStream, languageComponents);
    ExamplesCommon.printClusters(clusters);
  }

  @Test
  public void clusterWithDifferentAlgorithms() throws IOException {
    LanguageComponents languageComponents = LanguageComponents.load("English");

    // Perform clustering with each of the available algorithms.
    for (ClusteringAlgorithm algorithm :
        Arrays.asList(
            new LingoClusteringAlgorithm(),
            new STCClusteringAlgorithm(),
            new BisectingKMeansClusteringAlgorithm())) {
      System.out.println();
      System.out.println("Clustering implementation: " + algorithm.getClass().getSimpleName());
      List<Cluster<Document>> clusters =
          algorithm.cluster(ExamplesData.documentStream(), languageComponents);
      ExamplesCommon.printClusters(clusters);
    }
  }
}
