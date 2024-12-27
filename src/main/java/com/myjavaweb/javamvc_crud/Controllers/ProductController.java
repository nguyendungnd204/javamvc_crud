package com.myjavaweb.javamvc_crud.Controllers;
import com.myjavaweb.javamvc_crud.Models.Product;
import com.myjavaweb.javamvc_crud.dto.ProductDto;
import com.myjavaweb.javamvc_crud.repo.ProductRepository;
import com.myjavaweb.javamvc_crud.Services.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repo;
    private final ProductService productService;
    public ProductController(ProductRepository repo, ProductService productService) {
        this.repo = repo;
        this.productService = productService;
    }
    @GetMapping({"","/"})
    public String showProductList(Model model,
                                  @RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "sort", required = false) String sort) {
        List<Product> products = repo.findAll();
        if(search != null && !search.isEmpty()) {
            products = productService.searchProductsByName(search);
        }
        if(sort != null && !sort.isEmpty()) {
            if(sort.equals("asc")){
                products = productService.getAllProductsSortedByPriceASC();
            }else if(sort.equals("desc")){
                products = productService.getAllProductsSortedByPriceDesc();
            }
        }
        model.addAttribute("products",products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto",productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(
        @Valid @ModelAttribute ProductDto productDto,
                BindingResult result
    ){
        if(productDto.getImageFile().isEmpty()){
            result.addError(new FieldError("productDto","imageFile","The image file is empty"));
        }
        if (result.hasErrors()) {
            return "products/CreateProduct";
        }
        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_"+ image.getOriginalFilename();
        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);
        repo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());
            model.addAttribute("product", product);
            model.addAttribute("productDto", productDto);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String editProduct(Model model,@RequestParam int id ,@Valid @ModelAttribute
    ProductDto productDto, BindingResult result){
        try{
            Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            model.addAttribute("product",product);
            if(result.hasErrors()){
                return "products/EditProduct";
            }
            if(!productDto.getImageFile().isEmpty()){
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
                //delete file old
                try{
                    Files.delete(oldImagePath);
                } catch (Exception e) {
                   System.out.println("Exception: " + e.getMessage());
                }
                //save new image file
                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_"+ image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            repo.save(product);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/products";
    }
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){
        try{
            Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

            Path imagePath = Paths.get(product.getImageFileName());
            try{
                Files.delete(imagePath);
            }catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
            repo.delete(product);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return "redirect:/products";
    }

}
