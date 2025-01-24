package com.app.us_twogether.domain.category.subCategory;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api.base-url}/spaces")
public class SubCategoryController {

    @Autowired
    SubCategoryService subCategoryService;

    @PostMapping("/{spaceId}/category/{categoryId}/sub")
    public ResponseEntity<SubCategoryResponseDTO> createSubCategory(@PathVariable Long categoryId, @RequestBody @Valid SubCategoryRequestDTO subCategory) {

        SubCategoryResponseDTO newSubCategory = subCategoryService.createSubCategory(categoryId, subCategory);

        return ResponseEntity.ok(newSubCategory);
    }

    @PutMapping("/{spaceId}/category/{categoryId}/sub/{subCategoryId}")
    public ResponseEntity<SubCategoryResponseDTO> updateSubCategory(@PathVariable Long categoryId, @PathVariable Long subCategoryId, @RequestBody @Valid SubCategoryRequestDTO subCategory) {
        SubCategoryResponseDTO updatedSubCategory = subCategoryService.updateSubCategory(categoryId, subCategoryId, subCategory);

        return ResponseEntity.ok(updatedSubCategory);
    }

    @DeleteMapping("/{spaceId}/category/{categoryId}/sub/{subCategoryId}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Long subCategoryId) {
        subCategoryService.deletedSubCategory(subCategoryId);

        return ResponseEntity.ok("Category deletada com sucesso");
    }

    @GetMapping("/{spaceId}/category/{categoryId}/sub/{subCategoryId}")
    public ResponseEntity<SubCategoryResponseDTO> getSubCategory(@PathVariable Long categoryId, @PathVariable Long subCategoryId) {
        SubCategoryResponseDTO subCategory = subCategoryService.getReponseSubCategory(categoryId, subCategoryId);

        return ResponseEntity.ok(subCategory);
    }

    @GetMapping("/{spaceId}/category/{categoryId}/sub")
    public ResponseEntity<List<SubCategoryResponseDTO>> getAllSubCategoryFromCategory(@PathVariable Long categoryId) {
        List<SubCategoryResponseDTO> subCategory = subCategoryService.getAllSubCategoriesFromCategory(categoryId);

        return ResponseEntity.ok(subCategory);
    }

}
