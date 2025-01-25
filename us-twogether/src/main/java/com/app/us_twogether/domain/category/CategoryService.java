package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.service.SpaceService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryResponseDTO createCategory(Long spaceId, CategoryRequestDTO category){
        Space space = spaceService.findSpaceById(spaceId);

        Category newCategory = new Category();
        newCategory.setSpace(space);
        newCategory.setTitle(category.title());
        newCategory.setColor(category.color());
        newCategory.setCategoryType(category.categoryType());

        categoryRepository.save(newCategory);

        return categoryMapper.toResponseDTO(newCategory);
    }

    public CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO category){
        Category updatedCategory = findCategoryById(categoryId);

        updatedCategory.setTitle(category.title());
        updatedCategory.setColor(category.color());

        categoryRepository.save(updatedCategory);

        return categoryMapper.toResponseDTO(updatedCategory);
    }

    public void deletedCategory(Long categoryId) {
        Category existingCategory = findCategoryById(categoryId);

        categoryRepository.delete(existingCategory);
    }

    public CategoryResponseDTO getResponseCategory(Long categoryId){
        Category category = findCategoryById(categoryId);

        return categoryMapper.toResponseDTO(category);
    }

    public Category getCategory(Long categoryId){
        return findCategoryById(categoryId);
    }

    public List<CategoryResponseDTO> getAllCategoriesFromSpace(Long spaceId){
        List<Category> categories = categoryRepository.getAllCategoriesFromSpace(spaceId);

        return categories.stream().map(categoryMapper::toResponseDTO).toList();
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }
}
