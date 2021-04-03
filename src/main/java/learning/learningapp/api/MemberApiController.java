package learning.learningapp.api;


import learning.learningapp.domain.Member;
import learning.learningapp.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

// API를 만들때는 항상 엔티티를 파라미터로 받지 않는다. 엔티티를 외부로 노출해서는 안된다.

@RestController  // 데이터를 json 혹은 xml로 바로 보내는 역할을 한다.
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){   // @RequestBody json으로 들어온 body를 매핑에서 넣어준다.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid createMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")  //request dto 와 response dto 를 따로 만든다.
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    //update 요청 응답 클래스를 외부 파일로 만들어도 괜찮음. 오히려 그게 더 깔끔.
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor   //dto 에는 lombok annotation 을 많이 써도 괜찮다.
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }



    @Data
    static class createMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}











