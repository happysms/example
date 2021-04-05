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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// API를 만들때는 항상 엔티티를 파라미터로 받지 않는다. 엔티티를 외부로 노출해서는 안된다.

@RestController  // 데이터를 json 혹은 xml로 바로 보내는 역할을 한다.
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> result = new ArrayList<>();
        for (Member member : findMembers){
            result.add(new MemberDto(member.getName()));
        }

        //다른 방법
//        List<MemberDto> collect = findMembers.stream()
//                .map(m -> new MemberDto(m.getName()))
//                .collect(Collectors.toList());

        return new Result(result.size(), result);
    }

    //앞에서 리턴한 Result(result) 를 받아서 객체로 틀을 씌워준다. / 단순히 배열을 리턴해서 보내면 제이슨 배열 타입으로 생기므로 유연성 측면에서 효율적이지 않다.
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;   // 제이슨 array 타입이 아니기에 가능함.
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {  //api 스펙과 dto 가 매칭됨.
        private String name;
    }


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











