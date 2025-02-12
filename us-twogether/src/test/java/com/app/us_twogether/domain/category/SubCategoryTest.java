package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.category.subCategory.SubCategoryRequestDTO;
import com.app.us_twogether.domain.category.subCategory.SubCategoryResponseDTO;
import com.app.us_twogether.domain.category.subCategory.SubCategoryService;
import com.app.us_twogether.domain.space.SpaceService;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class SubCategoryTest {

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    private Long categoryId;

    @BeforeEach
    public void setup(){
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        userService.createUser(user);

        Long spaceId = spaceService.createSpace(user).spaceId();

        CategoryRequestDTO categoryDTO = new CategoryRequestDTO("Work", "#FF5733", CategoryType.EXPENSE);

        categoryId = categoryService.createCategory(spaceId, categoryDTO).categoryId();
    }

    @Test
    public void shouldCreateSubCategory_whenCredentialsAreValid() {
        SubCategoryRequestDTO subCategoryDTO = new SubCategoryRequestDTO("Equipments", "#FF5733");

        SubCategoryResponseDTO response = subCategoryService.createSubCategory(categoryId, subCategoryDTO);

        SubCategory subCategory = subCategoryService.getSubCategory(response.subCategoryId());

        assertNotNull(subCategory);
        assertNotNull(subCategory.getSubCategoryId());
        assertEquals("Equipments", subCategory.getTitle());
        assertEquals("#FF5733", subCategory.getColor());
    }

    @Test
    public void shouldUpdateSubCategory_whenCredentialsAreValid() {
        SubCategoryResponseDTO response = subCategoryService.createSubCategory(categoryId,
                new SubCategoryRequestDTO("Equipments", "#FF5733"));

        subCategoryService.updateSubCategory(categoryId, response.subCategoryId(),
                new SubCategoryRequestDTO("Transport", "#FF5734"));

        SubCategory subCategory = subCategoryService.getSubCategory(response.subCategoryId());

        assertNotNull(subCategory);
        assertNotNull(subCategory.getSubCategoryId());
        assertEquals("Transport", subCategory.getTitle());
        assertEquals("#FF5734", subCategory.getColor());
    }

    @Test
    public void shouldDeleteSubCategory_whenCredentialsAreValid() {
        SubCategoryRequestDTO subCategoryDTO = new SubCategoryRequestDTO("Equipments", "#FF5733");

        SubCategoryResponseDTO response = subCategoryService.createSubCategory(categoryId, subCategoryDTO);

        assertNotNull(response);
        assertNotNull(response.subCategoryId());
        assertEquals("Equipments", response.title());
        assertEquals("#FF5733", response.color());
    }

    @Test
    public void shouldGetAllSubCategoryFromCategory() {
        SubCategoryRequestDTO subCategoryDTO = new SubCategoryRequestDTO("Equipments", "#FF5733");

        SubCategoryResponseDTO response = subCategoryService.createSubCategory(categoryId, subCategoryDTO);

        assertNotNull(response);
        assertNotNull(response.subCategoryId());
        assertEquals("Equipments", response.title());
        assertEquals("#FF5733", response.color());
    }

}
