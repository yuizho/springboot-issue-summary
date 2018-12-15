package io.github.yuizho.springbootissuesummary.application.controllers;

import io.github.yuizho.springbootissuesummary.application.services.IssuesService;
import io.github.yuizho.springbootissuesummary.domain.models.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssuesController {
    @Autowired
    private IssuesService issuesService;

    @GetMapping("")
    public List<Issue> getIssues() {
        return issuesService.fetchIssues().asUnmodifiableList();
    }
}
