package dev.langchain4j.retriever;

import static java.util.Collections.singletonList;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import java.util.List;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class RetrieverTest implements WithAssertions {
    public static class TestRetriever implements Retriever<String> {
        @Override
        public List<String> findRelevant(String text) {
            return singletonList("abc");
        }
    }

    @Test
    void findRelevant() {
        Retriever<String> retriever = new TestRetriever();
        assertThat(retriever.findRelevant("test")).containsOnly("abc");

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> retriever.findRelevant("test", "test"))
                .withMessage("Not implemented");
    }

    @Test
    void should_convert_to_content_retriever() {

        // given
        Retriever<TextSegment> retriever = (text) -> singletonList(TextSegment.from(text));

        String query = "does not matter";

        // when
        ContentRetriever contentRetriever = retriever.toContentRetriever();

        // then
        assertThat(contentRetriever.retrieve(Query.from(query))).containsExactly(Content.from(query));
    }
}
