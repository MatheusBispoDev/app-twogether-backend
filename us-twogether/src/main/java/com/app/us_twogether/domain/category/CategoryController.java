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
    public ResponseEntity<CategoryResponseDTO> createCategory(@PathVariable Long spaceId, @RequestBody @Valid CategoryRequestDTO category) {
        CategoryResponseDTO newCategory = categoryService.createCategory(spaceId, category);

        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/{spaceId}/category/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryRequestDTO category) {
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(categoryId, category);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{spaceId}/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deletedCategory(categoryId);

        return ResponseEntity.ok("Categoria deletada com sucesso");
    }

    @GetMapping("/{spaceId}/category/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long categoryId) {
        CategoryResponseDTO category = categoryService.getResponseCategory(categoryId);

        return ResponseEntity.ok(category);
    }

    @GetMapping("/{spaceId}/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategoryFromSpace(@PathVariable Long spaceId) {
        List<CategoryResponseDTO> categories = categoryService.getAllCategoriesFromSpace(spaceId);

        return ResponseEntity.ok(categories);
    }
}
