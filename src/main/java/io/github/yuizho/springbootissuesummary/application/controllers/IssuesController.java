package io.github.yuizho.springbootissuesummary.application.controllers;

import io.github.yuizho.springbootissuesummary.application.services.IssuesService;
import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("/api/issues")
@Validated
public class IssuesController {
    @Autowired
    private IssuesService issuesService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Issues getIssues(
            @Min(1) @RequestParam(name = "page", required = false) Integer page,
            @Min(1) @RequestParam(name = "per_page", required = false) Integer perPage) {
        return issuesService.fetchIssues(Optional.ofNullable(page), Optional.ofNullable(perPage));
    }
}
