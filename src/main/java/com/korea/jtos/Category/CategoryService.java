package com.korea.jtos.Category;


import com.korea.jtos.Answer.Answer;
import com.korea.jtos.Comment.CommentRepository;
import com.korea.jtos.DataNotFoundException;
import com.korea.jtos.Question.Question;
import com.korea.jtos.User.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    

    public Category getCategory(Integer id) {
        Optional<Category> category = this.categoryRepository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException("category not found");
        }
    }

    public void create(String name) {
        Category c = new Category();
        c.setName(name);
        this.categoryRepository.save(c);
    }

    public void modify(Category category, String name) {
        category.setName(name);
        this.categoryRepository.save(category);
    }

    public void delete(Category category) {
        this.categoryRepository.delete(category);
    }

}
