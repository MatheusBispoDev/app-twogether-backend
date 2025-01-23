package com.app.us_twogether.domain.category.subCategory;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.category.CategoryMapper;
import com.app.us_twogether.domain.category.CategoryRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    public SubCategoryResponseDTO createSubCategory(Long categoryId, SubCategoryRequestDTO subcategory){
        Category category = findCategoryById(categoryId);

        SubCategory newSubCategory = new SubCategory();
        newSubCategory.setCategory(category);
        newSubCategory.setTitle(subcategory.title());
        newSubCategory.setColor(subcategory.color());

        subCategoryRepository.save(newSubCategory);

        return categoryMapper.toSubCategoryResponseDTO(newSubCategory);
    }

    public SubCategoryResponseDTO updateSubCategory(Long categoryId, Long subCategoryId, SubCategoryRequestDTO subcategory){
        SubCategory updatedSubCategory = getSubCategoryByCategory(categoryId, subCategoryId);

        updatedSubCategory.setTitle(subcategory.title());
        updatedSubCategory.setColor(subcategory.color());

        subCategoryRepository.save(updatedSubCategory);

        return categoryMapper.toSubCategoryResponseDTO(updatedSubCategory);
    }

    public void deletedSubCategory(Long subCategoryId){
        SubCategory subCategory = findSubCategoryById(subCategoryId);

        subCategoryRepository.delete(subCategory);
    }

    public SubCategoryResponseDTO getSubCategory(Long categoryId, Long subCategoryId){
        SubCategory subCategory = getSubCategoryByCategory(categoryId, subCategoryId);

        return categoryMapper.toSubCategoryResponseDTO(subCategory);
    }

    public List<SubCategoryResponseDTO> getAllSubCategoriesFromCategory(Long categoryId){
        List<SubCategory> subCategories = subCategoryRepository.getAllSubCategoriesFromCategory(categoryId);

        return subCategories.stream().map(categoryMapper::toSubCategoryResponseDTO).toList();
    }

    private SubCategory findSubCategoryById(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("Sub-Categoria não encontrada"));
    }

    private SubCategory getSubCategoryByCategory(Long categoryId, Long subCategoryId) {
        return subCategoryRepository.getSubCategoryFromCategory(categoryId, subCategoryId).orElseThrow(() -> new ResourceNotFoundException("Sub-Categoria não encontrada"));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }
}
