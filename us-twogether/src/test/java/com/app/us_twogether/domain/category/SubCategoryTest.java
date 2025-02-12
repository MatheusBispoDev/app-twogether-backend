package com.app.us_twogether.domain.category;

import com.app.us_twogether.domain.category.subCategory.SubCategory;
import com.app.us_twogether.domain.category.subCategory.SubCategoryRequestDTO;
import com.app.us_twogether.domain.category.subCategory.SubCategoryResponseDTO;
import com.app.us_twogether.domain.category.subCategory.SubCategoryService;
import com.app.us_twogether.domain.space.SpaceService;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        subCategoryService.deletedSubCategory(response.subCategoryId());

        assertThrows(ResourceNotFoundException.class, () -> subCategoryService.getSubCategory(response.subCategoryId()));
    }

    @Test
    public void shouldGetAllSubCategoryFromCategory() {
        List<SubCategoryRequestDTO> subCategoriesToCreate = List.of(
                new SubCategoryRequestDTO("Internet", "#FF5733"),
                new SubCategoryRequestDTO("Equipments", "#FF5733"),
                new SubCategoryRequestDTO("Tasks", "#FF5733"),
                new SubCategoryRequestDTO("Reunion", "#FF5733")
        );

        subCategoriesToCreate.forEach(subCategory -> subCategoryService.createSubCategory(categoryId, subCategory));

        List<SubCategoryResponseDTO> subCategories = subCategoryService.getAllSubCategoriesFromCategory(categoryId);

        assertNotNull(subCategories, "A lista de SubCategorias não deveria ser nula");
        assertEquals(4, subCategories.size(), "O número de SubCategorias deveria ser 4");

        List<String> actualTitles = subCategories.stream()
                .map(SubCategoryResponseDTO::title)
                .toList();

        List<String> expectedTitles = List.of("Internet", "Equipments", "Tasks", "Reunion");

        assertTrue(actualTitles.containsAll(expectedTitles), "A lista de SubCategorias deve conter os títulos esperados");
    }

}
