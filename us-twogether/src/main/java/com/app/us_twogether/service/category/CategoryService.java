package com.app.us_twogether.service.category;

import com.app.us_twogether.domain.category.Category;
import com.app.us_twogether.domain.category.CategoryDTO;
import com.app.us_twogether.domain.category.SubCategoryDTO;
import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.repository.category.CategoryRepository;
import com.app.us_twogether.service.SpaceService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SpaceService spaceService;

    public void createCategory(Long spaceId, CategoryDTO categoryDTO){
        Space space = spaceService.findSpaceById(spaceId);

        Category category = new Category();
        category.setSpace(space);
        category.setTitle(categoryDTO.title());
        category.setColor(categoryDTO.color());

        categoryRepository.save(category);
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO updatedDTO){
        Category existingCategory = findCategoryById(categoryId);

        existingCategory.setTitle(updatedDTO.title());
        existingCategory.setColor(updatedDTO.color());

        categoryRepository.save(existingCategory);

        return updatedDTO;
    }

    public void deletedCategory(Long categoryId) {
        Category existingCategory = findCategoryById(categoryId);

        categoryRepository.delete(existingCategory);
    }

    public CategoryDTO getCategory(Long categoryId){
        Category category = findCategoryById(categoryId);

        return castCategoryToDTO(category);
    }

    public List<CategoryDTO> getAllCategories(Long spaceId){
        List<Category> categories = categoryRepository.findByAllCategorySpace(spaceId);

        return categories.stream().map((this::castCategoryToDTO)).collect(Collectors.toList());
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    private CategoryDTO castCategoryToDTO(Category category){
        List<SubCategoryDTO> subCategories = category.getSubCategories().stream()
                .map(subCategory -> new SubCategoryDTO(subCategory.getSubCategoryId(), subCategory.getTitle(), subCategory.getTitle()))
                .collect(Collectors.toList());

        return new CategoryDTO(category.getCategoryId(), subCategories, category.getTitle(), category.getColor());
    }
}
