package com.projectTeam.therapist.boardService;

import com.projectTeam.therapist.model.PostDto;
import com.projectTeam.therapist.repository.PostRepository;
import com.projectTeam.therapist.validator.PostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostValidator postValidator;

    // @PageableDefault 을 이용해서 이 페이지로 접근했을 경우 기본적인 페이지 번호나 사이즈를 조절할 수 있다.
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue="") String searchText) {
//        Page<PostDto> posts = postRepository.findAll(pageable);
        Page<PostDto> posts = postRepository.findByPostTitleContainingOrPostContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(1, posts.getPageable().getPageNumber() - 4);
        int endPage = Math.min(posts.getTotalPages(), posts.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long postId) {
        if (postId == null) {
            model.addAttribute("post", new PostDto());
        } else {
            PostDto postDto = postRepository.findById(postId).orElse(null);
            model.addAttribute("post", postDto);
        }
        return "post/form";
    }
    
    @PostMapping("/form")
    public String formSubmit(@ModelAttribute("post") @Valid PostDto post, BindingResult bindingResult) {
        postValidator.validate(post, bindingResult);
        if (bindingResult.hasErrors()) {
            return "post/form";
        }
        postRepository.save(post);
        return "redirect:/post/list";
    }
}
