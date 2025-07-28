package com.vinsguru.business.util;

import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;

import java.util.List;

public class ElasticsearchUtil {
    public static Suggester buildCompletionSuggester(String suggestName, String field, String prefix, int limit) {
        var suggestFuzziness = SuggestFuzziness.of(b -> b
                .fuzziness(Constants.Fuzzy.LEVEL)
                .prefixLength(Constants.Fuzzy.PREFIX_LENGTH));
        CompletionSuggester completionSuggester = CompletionSuggester.of(b -> b
                .field(field)
                .size(limit)
                .fuzzy(suggestFuzziness)
                .skipDuplicates(true)
        );
        var fieldSuggester = FieldSuggester.of(b -> b
                .prefix(prefix)
                .completion(completionSuggester)
        );
        return Suggester.of(b -> b.suggesters(suggestName, fieldSuggester));
    }
}
