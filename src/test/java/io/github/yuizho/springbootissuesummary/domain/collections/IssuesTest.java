package io.github.yuizho.springbootissuesummary.domain.collections;

import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class IssuesTest {
    @Test
    public void addWorksCorrectly() {
        var issues = new Issues();

        var issue1 = new Issue("title1", "body1");
        var issues1 = issues.add(issue1);
        assertThat(issues1.asUnmodifiableList())
                .hasSize(1)
                .containsExactly(issue1);

        var issue2 = new Issue("title2", "body2");
        var issues2 = issues1.add(issue2);
        assertThat(issues2.asUnmodifiableList())
                .hasSize(2)
                .containsExactly(issue1, issue2);
    }

    @Test
    public void constructorWithListWorksCorrectly() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThat(new Issues(list).asUnmodifiableList())
                .hasSize(6)
                .containsExactly(
                        list.get(0),
                        list.get(1),
                        list.get(2),
                        list.get(3),
                        list.get(4),
                        list.get(5)
                );
    }

    @Test
    public void pagingWorksCorrectly() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThat(new Issues(list).asUnmodifiableList(1, 3))
                .hasSize(3)
                .containsExactly(list.get(0), list.get(1), list.get(2));
        assertThat(new Issues(list).asUnmodifiableList(2, 3))
                .hasSize(3)
                .containsExactly(list.get(3), list.get(4), list.get(5));
        assertThat(new Issues(list).asUnmodifiableList(1, 1))
                .hasSize(1)
                .containsExactly(list.get(0));
        assertThat(new Issues(list).asUnmodifiableList(6, 1))
                .hasSize(1)
                .containsExactly(list.get(5));
        assertThat(new Issues(list).asUnmodifiableList(1, 6))
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2),
                        list.get(3), list.get(4), list.get(5));
        // tailが実リスト数寄り大きい場合は、返せる分だけ返す
        assertThat(new Issues(list).asUnmodifiableList(1, 7))
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2),
                        list.get(3), list.get(4), list.get(5));
        // tailが実リスト数寄り大きい場合は、返せる分だけ返す
        assertThat(new Issues(list).asUnmodifiableList(2, 4))
                .hasSize(2)
                .containsExactly(list.get(4), list.get(5));
    }

    // TODO: エラーケースのテストを実装
}
