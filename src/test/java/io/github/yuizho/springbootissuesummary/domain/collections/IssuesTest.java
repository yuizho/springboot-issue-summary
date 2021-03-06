package io.github.yuizho.springbootissuesummary.domain.collections;

import io.github.yuizho.springbootissuesummary.domain.exceptions.SystemException;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

class IssuesTest {
    @Test
    void addWorksCorrectly() {
        var issues = new Issues();

        var issue1 = new Issue("title1", "body1");
        var issues1 = issues.add(issue1);
        assertThat(issues1.getIssues())
                .hasSize(1)
                .containsExactly(issue1);

        var issue2 = new Issue("title2", "body2");
        var issues2 = issues1.add(issue2);
        assertThat(issues2.getIssues())
                .hasSize(2)
                .containsExactly(issue1, issue2);
    }

    @Test
    void WhenNullIsPassedToConstructerWorksCorrectly() {
        var actual = new Issues(null);
        assertThat(actual.getIssues()).hasSize(0);
        assertThat(actual.isHasNext()).isFalse();
    }

    @Test
    void constructorWithListWorksCorrectly() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThat(new Issues(list).getIssues())
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
    void pagingWorksCorrectlyWhenBothParametersFilled() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(3)).getIssues())
                .hasSize(3)
                .containsExactly(list.get(0), list.get(1), list.get(2));
        assertThat(new Issues(list).asPaginated(Optional.of(2), Optional.of(3)).getIssues())
                .hasSize(3)
                .containsExactly(list.get(3), list.get(4), list.get(5));
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(1)).getIssues())
                .hasSize(1)
                .containsExactly(list.get(0));
        assertThat(new Issues(list).asPaginated(Optional.of(6), Optional.of(1)).getIssues())
                .hasSize(1)
                .containsExactly(list.get(5));
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(6)).getIssues())
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2),
                        list.get(3), list.get(4), list.get(5));
        // tailが実リスト数より大きい場合は、返せる分だけ返す
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(7)).getIssues())
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2),
                        list.get(3), list.get(4), list.get(5));
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(1000)).getIssues())
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2),
                        list.get(3), list.get(4), list.get(5));
        assertThat(new Issues(list).asPaginated(Optional.of(2), Optional.of(4)).getIssues())
                .hasSize(2)
                .containsExactly(list.get(4), list.get(5));
    }

    @Test
    void hasNextFieldIsSetCorrectlyWhenPagingMethodIsUsed() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(3)).isHasNext())
                .isEqualTo(true);
        assertThat(new Issues(list).asPaginated(Optional.of(2), Optional.of(3)).isHasNext())
                .isEqualTo(false);
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(1)).isHasNext())
                .isEqualTo(true);
        assertThat(new Issues(list).asPaginated(Optional.of(6), Optional.of(1)).isHasNext())
                .isEqualTo(false);
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(6)).isHasNext())
                .isEqualTo(false);
        // tailが実リスト数より大きいケース
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(7)).isHasNext())
                .isEqualTo(false);
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(1000)).isHasNext())
                .isEqualTo(false);
        assertThat(new Issues(list).asPaginated(Optional.of(2), Optional.of(4)).isHasNext())
                .isEqualTo(false);
    }

    @Test
    void pagingWorksCorrectlyWhenPageJustFilled() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThat(new Issues(list).asPaginated(Optional.empty(), Optional.of(3)).getIssues())
                .hasSize(3)
                .containsExactly(list.get(0), list.get(1), list.get(2));
        assertThat(new Issues(list).asPaginated(Optional.empty(), Optional.of(1)).getIssues())
                .hasSize(1)
                .containsExactly(list.get(0));
        assertThat(new Issues(list).asPaginated(Optional.empty(), Optional.of(6)).getIssues())
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2),
                        list.get(3), list.get(4), list.get(5));
        assertThat(new Issues(list).asPaginated(Optional.empty(), Optional.of(7)).getIssues())
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2),
                        list.get(3), list.get(4), list.get(5));
    }

    @Test
    void pagingWorksCorrectlyWhenPerPageJustFilledAndListHasOver10Items() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6"),
                new Issue("title7", "title7"),
                new Issue("title8", "title8"),
                new Issue("title9", "title9"),
                new Issue("title10", "title10"),
                new Issue("title11", "title11"),
                new Issue("title12", "title12"),
                new Issue("title13", "title13")
        );

        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.empty()).getIssues())
                .hasSize(10)
                .containsExactly(list.get(0), list.get(1), list.get(2), list.get(3),
                        list.get(4), list.get(5), list.get(6), list.get(7),
                        list.get(8), list.get(9));
        assertThat(new Issues(list).asPaginated(Optional.of(2), Optional.empty()).getIssues())
                .hasSize(3)
                .containsExactly(list.get(10), list.get(11), list.get(12));
    }

    @Test
    void pagingWorksCorrectlyWhenPerPageJustFilledAndListHasLessThan10Items() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.empty()).getIssues())
                .hasSize(6)
                .containsExactly(list.get(0), list.get(1), list.get(2), list.get(3),
                        list.get(4), list.get(5));
    }

    @Test
    void pagingReturnsEmptyListWhenIssuesIsEmpty() {
        var list = new ArrayList<Issue>();

        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.empty()).getIssues())
                .hasSize(0);
        assertThat(new Issues(list).asPaginated(Optional.of(1), Optional.of(1)).getIssues())
                .hasSize(0);
    }

    @Test
    void pagingReturnsEmptyListWhenSpecifiedStartIndexOfRangeExceedsList() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6"),
                new Issue("title7", "title7"),
                new Issue("title8", "title8"),
                new Issue("title9", "title9"),
                new Issue("title10", "title10"),
                new Issue("title11", "title11"),
                new Issue("title12", "title12"),
                new Issue("title13", "title13")
        );

        assertThat(new Issues(list).asPaginated(Optional.of(2), Optional.of(13)).getIssues())
                .hasSize(0);
        assertThat(new Issues(list).asPaginated(Optional.of(3), Optional.empty()).getIssues())
                .hasSize(0);
    }

    @Test
    void pagingThrowsExceptionWhenZeroOrMinusValueIsSetToParameter() {
        var list = List.of(
                new Issue("title1", "title1"),
                new Issue("title2", "title2"),
                new Issue("title3", "title3"),
                new Issue("title4", "title4"),
                new Issue("title5", "title5"),
                new Issue("title6", "title6")
        );

        assertThatThrownBy(() -> new Issues(list).asPaginated(Optional.of(0), Optional.of(10)).getIssues())
                .isInstanceOfSatisfying(SystemException.class, e -> {
                   assertThat(e.getMessage()).isEqualTo("0 or minus value was set to pagination parameter. probably some validation of controller is not enough.");
                });
        assertThatThrownBy(() -> new Issues(list).asPaginated(Optional.of(-1), Optional.empty()).getIssues())
                .isInstanceOfSatisfying(SystemException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("0 or minus value was set to pagination parameter. probably some validation of controller is not enough.");
                });
        assertThatThrownBy(() -> new Issues(list).asPaginated(Optional.of(1), Optional.of(0)).getIssues())
                .isInstanceOfSatisfying(SystemException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("0 or minus value was set to pagination parameter. probably some validation of controller is not enough.");
                });
        assertThatThrownBy(() -> new Issues(list).asPaginated(Optional.empty(), Optional.of(-1)).getIssues())
                .isInstanceOfSatisfying(SystemException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("0 or minus value was set to pagination parameter. probably some validation of controller is not enough.");
                });

    }
}
