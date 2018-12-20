package io.github.yuizho.springbootissuesummary.application.controllers;

import io.github.yuizho.springbootissuesummary.application.services.IssuesService;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IssuesController.class)
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class IssuesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssuesService issuesService;

    @Test
    public void defaultDoc() throws Exception {
        when(issuesService.fetchIssues(any(), any()))
                .thenReturn(
                            new Issues(List.of(
                                    new Issue("Task input", "BootJar`'s `bootInf` property"),
                                    new Issue("Improve Sp", "We prepared article about migr")
                            ), true));
        this.mockMvc.perform(
                get("/api/issues?page=1&per_page=2").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(document("issues",
                            requestParameters(
                                    parameterWithName("page")
                                            .optional()
                                            .description("取得するページ番号 (省略可能, 型: 数値, デフォルト値: 1, 入力可能範囲: 1~)"),
                                    parameterWithName("per_page")
                                            .optional()
                                            .description("一度に取得するIssueの数 (省略可能, 型: 数値, デフォルト値: 10, 入力可能範囲: 1~)")
                            ),
                            responseFields(
                                    fieldWithPath("has_next").type(JsonFieldType.BOOLEAN).description("次のページが存在する場合はtrue, 次のページが存在しない場合はfalse"),
                                    fieldWithPath("issues[]").type(JsonFieldType.ARRAY)
                                            .description("Issue情報の概要のリスト。page=101&per_page=1など、パラメータに返却可能なページ数(100)を越えた値が指定された場合、空のリストを返す。"),
                                    fieldWithPath("issues[].title").type(JsonFieldType.STRING).description("Issueのtitle (先頭10文字)"),
                                    fieldWithPath("issues[].body").type(JsonFieldType.STRING).description("Issueのbody (先頭30文字)")
                            )
                        ));
    }

    @Test
    public void noParamDoc() throws Exception {
        when(issuesService.fetchIssues(any(), any()))
                .thenReturn(
                        new Issues(List.of(
                                new Issue("Task input", "BootJar`'s `bootInf` property"),
                                new Issue("Improve Sp", "We prepared article about migr"),
                                new Issue("title3", "body3"),
                                new Issue("title4", "body4"),
                                new Issue("title5", "body5"),
                                new Issue("title6", "body6"),
                                new Issue("title7", "body7"),
                                new Issue("title8", "body8"),
                                new Issue("title9", "body9"),
                                new Issue("title10", "body10")
                        ), true));
        this.mockMvc.perform(
                get("/api/issues").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(document("issues_no_param",
                        requestParameters(
                                parameterWithName("page")
                                        .optional()
                                        .description(""),
                                parameterWithName("per_page")
                                        .optional()
                                        .description("")
                        ),
                        responseFields(
                                fieldWithPath("has_next").type(JsonFieldType.BOOLEAN).description(""),
                                fieldWithPath("issues[]").type(JsonFieldType.ARRAY).description(""),
                                fieldWithPath("issues[].title").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("issues[].body").type(JsonFieldType.STRING).description("")
                        )
                ));
    }

    @Test
    public void perPageBiggerThanRamining() throws Exception {
        when(issuesService.fetchIssues(any(), any()))
                .thenReturn(
                        new Issues(List.of(
                                new Issue("Task input", "BootJar`'s `bootInf` property"),
                                new Issue("Improve Sp", "We prepared article about migr"),
                                new Issue("title3", "body3"),
                                new Issue("title4", "body4"),
                                new Issue("title5", "body5"),
                                new Issue("title6", "body6"),
                                new Issue("title7", "body7"),
                                new Issue("title8", "body8"),
                                new Issue("title9", "body9"),
                                new Issue("title10", "body10")
                        ), false));
        this.mockMvc.perform(
                get("/api/issues?page=4&per_page=30").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(document("issues_bigger_than_remaining",
                        requestParameters(
                                parameterWithName("page")
                                        .optional()
                                        .description(""),
                                parameterWithName("per_page")
                                        .optional()
                                        .description("")
                        ),
                        responseFields(
                                fieldWithPath("has_next").type(JsonFieldType.BOOLEAN).description(""),
                                fieldWithPath("issues[]").type(JsonFieldType.ARRAY).description(""),
                                fieldWithPath("issues[].title").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("issues[].body").type(JsonFieldType.STRING).description("")
                        )
                ));
    }

    @Test
    public void exceedsMaxPage() throws Exception {
        when(issuesService.fetchIssues(any(), any()))
                .thenReturn(
                        new Issues(List.of(), false));
        this.mockMvc.perform(
                get("/api/issues?page=101&per_page=1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(document("issues_exceeds_max",
                        requestParameters(
                                parameterWithName("page")
                                        .optional()
                                        .description(""),
                                parameterWithName("per_page")
                                        .optional()
                                        .description("")
                        ),
                        responseFields(
                                fieldWithPath("has_next").type(JsonFieldType.BOOLEAN).description(""),
                                fieldWithPath("issues[]").type(JsonFieldType.ARRAY).description("")
                        )
                ));
    }
}
