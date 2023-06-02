package ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import ecom.dto.ProductDTO;
import ecom.service.ProductService;
import jakarta.websocket.OnError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ecom.model.Category;
import ecom.model.Product;
import ecom.service.CategoryService;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	private static String uploadDir = System.getProperty("user.dir")+"/src/main/resources/static/productImages/";

	@GetMapping("/admin")
	public String adminHome() {
		return "adminHome";
	}
	
	
	@GetMapping("admin/categories")
	public String getCat(Model model) {
		model.addAttribute("categories", categoryService.getAllCategory());
		return "categories";
	}
	
	@GetMapping("/admin/categories/add")
	public String getCatAdd(Model model) {
		model.addAttribute("category", new Category());
		
		return "categoriesAdd";
	}
	
	@PostMapping("admin/categories/add")
	public String postCatAdd(@ModelAttribute("category") Category category) {
		categoryService.addCategory(category);
		return "redirect:/admin/categories";
		
	}
	
	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCat(@PathVariable int id) {
		
		categoryService.removeCategoryById(id);
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/update/{id}")
	public String updateCat(@PathVariable int id, Model model) {
		Optional<Category> categoryById = categoryService.getCategoryById(id);
		model.addAttribute("category", categoryById.get());
		
		return "categoriesAdd";
	}
	
	//product
	
	@GetMapping("/admin/products")
	public String products(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "products";
	}
	
	@GetMapping("/admin/products/add")
	public String productAddGet(Model model) {
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("categories", categoryService.getAllCategory());
		return "productAdd";
	}
	@PostMapping("/admin/products/add")
	public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
								 @RequestParam("productImage")MultipartFile file,
								 @RequestParam("imgName") String imgName) throws IOException {

		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		product.setImageName(imgName);

		String imageUUID;
		if(!file.isEmpty()){
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		}else{
			imageUUID = imgName;
		}
		product.setImageName(imageUUID);
		productService.addProduct(product);
		return "redirect:/admin/products ";
	}
	@GetMapping("admin/product/delete/{id}")
	public String deleteProduct(@PathVariable int id){
		Product product = productService.getProductById(id).get();
		File productImage = new File(uploadDir+product.getImageName());
		productImage.delete();
		productService.removeProductById(id);

		return "redirect:/admin/products";
	}

	@GetMapping("admin/product/update/{id}")
	public String updateProduct(@PathVariable int id, Model model){
		Product product = productService.getProductById(id).get();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setCategoryId(product.getCategory().getId());
		productDTO.setPrice(product.getPrice());
		productDTO.setWeight(product.getWeight());
		productDTO.setDescription(product.getDescription());
		productDTO.setImageName(product.getImageName());

		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("productDTO", productDTO);

		return "productAdd";
	}
}
