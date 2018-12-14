package io.github.yuizho.springbootissuesummary.application.controllers;

import io.github.yuizho.springbootissuesummary.domain.collections.Issues;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssuesController {
    @GetMapping("")
    public List<Issue> getIssues() {
        // TODO: move to service class
        return new Issues(List.of(new Issue("title", "body")))
                .asUnmodifiableList();
    }
}
