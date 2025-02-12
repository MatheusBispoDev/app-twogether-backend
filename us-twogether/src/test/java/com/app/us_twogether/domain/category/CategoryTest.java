package com.app.us_twogether.domain.category;

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
public class CategoryTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private UserService userService;

    private Long spaceId;

    @BeforeEach
    public void setup(){
        User user = new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US");
        this.userService.createUser(user);

        this.spaceId = this.spaceService.createSpace(user).spaceId();
    }

    @Test
    public void shouldCreateCategory_whenCredentialsAreValid() {
        CategoryRequestDTO categoryDTO = new CategoryRequestDTO("Work", "#FF5733", CategoryType.EXPENSE);

        CategoryResponseDTO response = categoryService.createCategory(spaceId, categoryDTO);

        Category category = categoryService.getCategory(response.categoryId());

        assertNotNull(category);
        assertNotNull(category.getCategoryId());
        assertEquals("Work", category.getTitle());
        assertEquals("#FF5733", category.getColor());
        assertEquals(CategoryType.EXPENSE, category.getCategoryType());
    }

    @Test
    public void shouldUpdateCategory_whenCredentialsAreValid(){
        CategoryResponseDTO response = categoryService.createCategory(spaceId, new CategoryRequestDTO("Work", "#FF5733", CategoryType.EXPENSE));

        categoryService.updateCategory(response.categoryId(), new CategoryRequestDTO("Home", "#FF5734", CategoryType.ACTIVITY));

        Category category = categoryService.getCategory(response.categoryId());

        assertNotNull(category);
        assertNotNull(category.getCategoryId());
        assertEquals("Home", category.getTitle());
        assertEquals("#FF5734", category.getColor());
        assertEquals(CategoryType.ACTIVITY, category.getCategoryType());
    }

    @Test
    public void shouldDeleteCategory_whenCredentialsAreValid(){
        CategoryResponseDTO response = categoryService.createCategory(spaceId, new CategoryRequestDTO("Work", "#FF5733", CategoryType.EXPENSE));

        categoryService.deletedCategory(response.categoryId());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategory(response.categoryId()));
    }

    @Test
    public void shouldGetAllCategoryFromSpace(){
        List<CategoryRequestDTO> categoriesToCreate = List.of(
                new CategoryRequestDTO("Work", "#FF5733", CategoryType.EXPENSE),
                new CategoryRequestDTO("Home", "#FF5733", CategoryType.EXPENSE),
                new CategoryRequestDTO("Shopping", "#FF5733", CategoryType.EXPENSE),
                new CategoryRequestDTO("Hospital", "#FF5733", CategoryType.EXPENSE),
                new CategoryRequestDTO("Family", "#FF5733", CategoryType.EXPENSE)
        );

        categoriesToCreate.forEach(category -> categoryService.createCategory(spaceId, category));

        List<CategoryResponseDTO> categories = categoryService.getAllCategoriesFromSpace(spaceId);

        assertNotNull(categories, "A lista de categorias não deveria ser nula");
        assertEquals(5, categories.size(), "O número de categorias deveria ser 5");
        List<String> actualTitles = categories.stream()
                .map(CategoryResponseDTO::title)
                .toList();

        List<String> expectedTitles = List.of("Work", "Home", "Shopping", "Hospital", "Family");

        assertTrue(actualTitles.containsAll(expectedTitles), "A lista de categorias deve conter os títulos esperados");

    }
}
