package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;



//==> 회원관리 Controller
@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping(value="addProductView", method=RequestMethod.POST)
	public ModelAndView addProductView() throws Exception {

		System.out.println("/product/addProductView");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/addProductView.jsp");
		
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public ModelAndView addProduct( @ModelAttribute("product") Product product ) throws Exception {

		System.out.println("/product/addProduct");
		
		//Business Logic 후 Model(data) / View(jsp) 정보를 갖는 ModelAndView 생성
		product.setManuDate(product.getManuDate().replace("-", "")); //because of DB type or size...
		productService.addProduct(product);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/product/listProduct");
		
		return modelAndView;
	}
	
	
	@RequestMapping(value="getProduct", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getProduct( @RequestParam("prodNo") int prodNo , Model model ) throws Exception {
		
		System.out.println("/product/getProduct");
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("product", product);
		modelAndView.setViewName("/product/getProduct.jsp");
		
		return modelAndView;
	}
	
	
	@RequestMapping(value="updateProductView", method = RequestMethod.GET)
	public ModelAndView updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/product/updateProductView");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		
		// Model 과 View 연결
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("product", product);
		modelAndView.setViewName("/product/updateProductView.jsp");
		
		return modelAndView; // 이거 확인
	}
	
	

	@RequestMapping(value="updateProduct", method = RequestMethod.POST)
	public ModelAndView updateProduct( @ModelAttribute("product") Product product, @RequestParam("menu") String menu) throws Exception{
	
		System.out.println("/product/updateProduct");
			
		//Business Logic 수행
		productService.updateProduct(product);
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("menu", menu);
		modelAndView.setViewName("/product/getProduct");
		
		return modelAndView; 
	}
	
	
	@RequestMapping(value="listProduct")
	public ModelAndView listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/product/listProduct");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		modelAndView.setViewName("/product/listProduct.jsp");
		
		return modelAndView;
	}
	
}