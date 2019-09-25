package io.xxx.alouette.site.web;

import io.xxx.alouette.site.service.MemberService;
import io.xxx.alouette.site.web.form.MemberRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public Long create(@RequestBody MemberRequest memberRequest) {
        return memberService.create(memberRequest);
    }
}
