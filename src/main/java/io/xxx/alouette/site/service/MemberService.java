package io.xxx.alouette.site.service;

import io.xxx.alouette.data.MemberRepository;
import io.xxx.alouette.entity.MemberEntity;
import io.xxx.alouette.site.web.form.MemberRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long create(MemberRequest memberRequest) {
        MemberEntity memberEntity = new MemberEntity();
        BeanUtils.copyProperties(memberRequest, memberEntity);
        return memberRepository.save(memberEntity).getId();
    }
}
