package org.carrot2.source.etools;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.carrot2.core.Document;
import org.carrot2.core.test.QueryableDocumentSourceTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junitext.Prerequisite;
import org.junitext.runners.AnnotationRunner;

import com.google.common.collect.Sets;

/**
 * Test cases for {@link EToolsDocumentSource}.
 */
@RunWith(AnnotationRunner.class)
public class EToolsDocumentSourceTest extends
    QueryableDocumentSourceTestBase<EToolsDocumentSource>
{
    @Override
    public Class<EToolsDocumentSource> getComponentClass()
    {
        return EToolsDocumentSource.class;
    }

    @Test
    @Prerequisite(requires = "externalApiTestsEnabled")
    public void testDataSources() throws Exception
    {
        runQuery("apache", 50);

        final Set<String> sources = Sets.newHashSet();
        for (Document document : getDocuments())
        {
            final List<String> documentSources = document.getField(Document.SOURCES);
            assertThat(documentSources).isNotEmpty();
            sources.addAll(documentSources);
        }

        assertThat(sources.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testDataSourceResultsCount()
    {
        final EToolsDocumentSource source = new EToolsDocumentSource();

        checkDataSourceResultsCount(source, 0, 0);
        checkDataSourceResultsCount(source, 50, 20);
        checkDataSourceResultsCount(source, 100, 20);
        checkDataSourceResultsCount(source, 120, 30);
        checkDataSourceResultsCount(source, 200, 30);
        checkDataSourceResultsCount(source, 250, 40);
        checkDataSourceResultsCount(source, 400, 40);
        checkDataSourceResultsCount(source, 450, 40);
        checkDataSourceResultsCount(source, 1000, 40);
    }

    private void checkDataSourceResultsCount(EToolsDocumentSource source, int results,
        int expectedDataSourceResultsCount)
    {
        source.results = results;
        assertEquals("Data source results count", expectedDataSourceResultsCount, source
            .getDataSourceResultsCount());
    }
}
