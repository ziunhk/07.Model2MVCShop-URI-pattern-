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



//==> ȸ������ Controller
@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method ���� ����
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
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
			//==>Eclipse workspace / Project ����� ������ ��
			//String temDir = "D:\\workspace03(Project & analysis)\\9941.1.1.Model2MVCShop(ins)\\WebContent\\images\\uploadFiles\\";
			String temDir = "C:\\Users\\Admin\\git\\07.Model2MVCShop(URI,pattern)\\07.Model2MVCShop(URI,pattern)\\WebContent\\images\\uploadFiles\\";
			//String temDir = ".";
			//String temDir2 = "/uploadFiles/";
			
			DiskFileUpload fileUpload = new DiskFileUpload();
			fileUpload.setRepositoryPath(temDir);
			//setSizeThreshold�� ũ�⸦ ����ԵǸ� ������ ��ġ�� �ӽ÷� �����Ѵ�.
			fileUpload.setSizeMax(1024 * 1024 * 10);
			//�ִ� 1�ް� ���� ���ε� ���� (1024 * 1024 * 100) <- 100MB
			fileUpload.setSizeThreshold(1024 * 100); // �ѹ��� 100k ������ �޸𸮿� ����
			
			if(request.getContentLength() < fileUpload.getSizeMax()){
				
				//Product product = new Product();
				product = new Product();
				
				StringTokenizer token = null;
				
				//parseRequest()�� Fileitem�� �����ϰ� �ִ� List Ÿ���� �����Ѵ�.
				List fileItemList = fileUpload.parseRequest(request);
				int Size = fileItemList.size(); // html page���� ���� ������ ������ ���Ѵ�.
				for(int i = 0; i < Size; i++){
					FileItem fileItem = (FileItem)fileItemList.get(i);
					// isFormField()�� ���ؼ� ������������ �Ķ�������� �����Ѵ�. �Ķ���Ͷ�� true
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
					}else{ // ���������̸�
						// out.print("���� : "+fileItem.getFieldName() + " = " +fileItem.getName());
						// out.print("("+fileItem.getSize() + "byte)<br>");
							
						if(fileItem.getSize() > 0){ //������ �����ϴ� if
							int idx = fileItem.getName().lastIndexOf("\\");
							// getName()�� ��θ� �� �������� ������ lastIndexOf �� �߶󳽴�.
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
			}else{ // ���ε��ϴ� ������ setSizeMax ���� ū ���
				int overSize = (request.getContentLength() / 1000000);
				System.out.println("<script>alert('������ ũ��� 1MB���� �Դϴ�. �ø��� ���� �뷮�� "+overSize+" = MB�Դϴ�.");
				System.out.println("history.back();</script>");
			}
		}else{
			System.out.println("���ڵ� Ÿ���� multipart/form-data�� �ƴմϴ�");
		}
	
		System.out.println("\n\n\n\198236981698"+product);
	
		//Business Logic �� Model(data) / View(jsp) ������ ���� ModelAndView ����
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
		// Model �� View ����
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
		
		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("product", product);
		modelAndView.setViewName("/product/updateProductView.jsp");
		
		return modelAndView; // �̰� Ȯ��
	}
	
	

	@RequestMapping(value="updateProduct", method = RequestMethod.POST)
	public ModelAndView updateProduct( @ModelAttribute("product") Product product, @RequestParam("menu") String menu) throws Exception{
	
		System.out.println("/product/updateProduct");
			
		//Business Logic ����
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
		
		// Business logic ����
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		modelAndView.setViewName("/product/listProduct.jsp");
		
		return modelAndView;
	}
	
}