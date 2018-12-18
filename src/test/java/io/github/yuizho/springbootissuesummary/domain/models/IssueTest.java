package io.github.yuizho.springbootissuesummary.domain.models;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class IssueTest {
    @Test
    public void titleAndBodyIsLessThanMaxLength() {
        var issues = new Issue("title", "body");
        assertThat(issues.getTitle()).isEqualTo("title");
        assertThat(issues.getBody()).isEqualTo("body");
    }

    @Test
    public void titleAndBodyIsNull() {
        var issues = new Issue(null, null);
        assertThat(issues.getTitle()).isEqualTo("");
        assertThat(issues.getBody()).isEqualTo("");
    }

    @Test
    public void titleAndBodyExceedMaxLength() {
        var issues = new Issue("12345678901", "1234567890123456789012345678901");
        assertThat(issues.getTitle())
                .hasSize(10)
                .isEqualTo("1234567890");
        assertThat(issues.getBody())
                .hasSize(30)
                .isEqualTo("123456789012345678901234567890");
    }

    @Test
    public void titleAndBodyis2byteChar() {
        var issues = new Issue("あいうえおあいうえおあ", "あいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあ");
        assertThat(issues.getTitle())
                .hasSize(10)
                .isEqualTo("あいうえおあいうえお");
        assertThat(issues.getBody())
                .hasSize(30)
                .isEqualTo("あいうえおあいうえおあいうえおあいうえおあいうえおあいうえお");
    }
}
