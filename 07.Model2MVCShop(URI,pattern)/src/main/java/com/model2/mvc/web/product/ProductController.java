package com.model2.mvc.web.product;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
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
import com.model2.mvc.service.product.impl.ProductServiceImpl;



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
	public ModelAndView addProduct( @ModelAttribute("product") Product product, 
														HttpServletRequest request, 
														HttpServletResponse response) throws Exception {

		System.out.println("/product/addProduct");
		
		if(FileUpload.isMultipartContent(request)){
			//==>Eclipse workspace / Project 변경시 변경할 것
			//String temDir = "D:\\workspace03(Project & analysis)\\9941.1.1.Model2MVCShop(ins)\\WebContent\\images\\uploadFiles\\";
			String temDir = "C:\\Users\\Admin\\git\\07.Model2MVCShop(URI,pattern)\\07.Model2MVCShop(URI,pattern)\\WebContent\\images\\uploadFiles\\";
			//String temDir = ".";
			//String temDir2 = "/uploadFiles/";
			
			DiskFileUpload fileUpload = new DiskFileUpload();
			fileUpload.setRepositoryPath(temDir);
			//setSizeThreshold의 크기를 벗어나게되면 지정한 위치에 임시로 저장한다.
			fileUpload.setSizeMax(1024 * 1024 * 10);
			//최대 1메가 까지 업로드 가능 (1024 * 1024 * 100) <- 100MB
			fileUpload.setSizeThreshold(1024 * 100); // 한번에 100k 까지는 메모리에 저장
			
			if(request.getContentLength() < fileUpload.getSizeMax()){
				
				//Product product = new Product();
				product = new Product();
				
				StringTokenizer token = null;
				
				//parseRequest()는 Fileitem을 포함하고 있는 List 타입을 리턴한다.
				List fileItemList = fileUpload.parseRequest(request);
				int Size = fileItemList.size(); // html page에서 받은 값들의 개수를 구한다.
				for(int i = 0; i < Size; i++){
					FileItem fileItem = (FileItem)fileItemList.get(i);
					// isFormField()를 통해서 파일형식인지 파라미터인지 구분한다. 파라미터라면 true
					if(fileItem.isFormField()){
						if(fileItem.getFieldName().equals("manuDate")){
							token = new StringTokenizer(fileItem.getString("euc-kr"), "-");
							String manuDate = token.nextToken()+token.nextToken()+token.nextToken();
							product.setManuDate(manuDate);
						}
						else if(fileItem.getFieldName().equals("prodName")){
							product.setProdName(fileItem.getString("euc-kr"));
						}
						else if(fileItem.getFieldName().equals("prodDetail")){
							product.setProdDetail(fileItem.getString("euc-kr"));
						}
						else if(fileItem.getFieldName().equals("price")){
							product.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
						}
					}else{ // 파일형식이면
						// out.print("파일 : "+fileItem.getFieldName() + " = " +fileItem.getName());
						// out.print("("+fileItem.getSize() + "byte)<br>");
							
						if(fileItem.getSize() > 0){ //파일을 저장하는 if
							int idx = fileItem.getName().lastIndexOf("\\");
							// getName()은 경로를 다 가져오기 때문에 lastIndexOf 로 잘라낸다.
							if(idx == -1){
								idx = fileItem.getName().lastIndexOf("/");
							}
							String fileName = fileItem.getName().substring(idx+1);
							product.setFileName(fileName);
							try{
								File uploadedFile = new File(temDir, fileName);
								fileItem.write(uploadedFile);
							}catch(IOException e){
								System.out.println(e);
							}
						}else{
							product.setFileName("../../images/empty.GIF");
						}
					}// else
				}// for
				
//				ProductServiceImpl service = new ProductServiceImpl();
//				service.addProduct(product);
					
				request.setAttribute("prodvo", product);
			}else{ // 업로드하는 파일이 setSizeMax 보다 큰 경우
				int overSize = (request.getContentLength() / 1000000);
				System.out.println("<script>alert('파일의 크기는 1MB까지 입니다. 올리신 파일 용량은 "+overSize+" = MB입니다.");
				System.out.println("history.back();</script>");
			}
		}else{
			System.out.println("인코딩 타입이 multipart/form-data가 아닙니다");
		}
	
		System.out.println("\n\n\n\198236981698"+product);
	
		//Business Logic 후 Model(data) / View(jsp) 정보를 갖는 ModelAndView 생성
		product.setManuDate(product.getManuDate().replace("-", "")); //because of DB type or size...
		productService.addProduct(product);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(product);
		modelAndView.setViewName("/product/addProduct.jsp");
		
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