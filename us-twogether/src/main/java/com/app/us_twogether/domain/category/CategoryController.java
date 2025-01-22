package com.app.us_twogether.domain.category;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api.base-url}/spaces")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/{spaceId}/category")
    public ResponseEntity<CategoryDTO> createCategory(@PathVariable Long spaceId, @RequestBody @Valid CategoryDTO categoryDTO) {
        categoryService.createCategory(spaceId, categoryDTO);

        return ResponseEntity.ok(categoryDTO);
    }

    @PutMapping("/{spaceId}/category/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryDTO updatedCategoryDTO) {
        CategoryDTO category = categoryService.updateCategory(categoryId, updatedCategoryDTO);

        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{spaceId}/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deletedCategory(categoryId);

        return ResponseEntity.ok("Category deletada com sucesso");
    }

    @GetMapping("/{spaceId}/category/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long categoryId) {
        CategoryDTO category = categoryService.getCategory(categoryId);

        return ResponseEntity.ok(category);
    }

    @GetMapping("/{spaceId}/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategoryFromSpace(@PathVariable Long spaceId) {
        List<CategoryDTO> categories = categoryService.getAllCategories(spaceId);

        return ResponseEntity.ok(categories);
    }
}
