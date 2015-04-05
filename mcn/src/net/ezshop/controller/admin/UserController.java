package net.ezshop.controller.admin;

import javax.annotation.Resource;

import net.ezshop.Message;
import net.ezshop.Page;
import net.ezshop.Pageable;
import net.ezshop.entity.User;
import net.ezshop.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("userController")
@RequestMapping("/admin/user")
public class UserController extends BaseController{

	@Resource(name="userServiceImpl")
	private UserService userService;
	
	/**
	 * add
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(ModelMap model){
		model.addAttribute("key", "123456");
		return "/admin/user/add";
	}
	

	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String save(User user,RedirectAttributes redirectAttributes){
		System.out.println("redirecAttributes is:"+redirectAttributes.getFlashAttributes().keySet().toArray());
		userService.save(user);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	/**
	 * edit
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Long id ,ModelMap model){
		model.addAttribute("user", userService.find(id));
		return "/admin/user/edit";
	}
	
	/**
	 * update
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="update",method=RequestMethod.POST)
	public String update(User user,RedirectAttributes redirectAttributes){
		if(! isValid(user)){
			return ERROR_VIEW;
		}
		User pUser =userService.find(user.getId());
		if(pUser == null){
			return ERROR_VIEW;
		}
		System.out.println("update user age:"+user.getAge());
		userService.update(user);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	/**
	 * list
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Pageable pageable,ModelMap model){
		Page<User> page=userService.findPage(pageable);
		model.addAttribute("page", page);
		return "/admin/user/list";
	}
	
	/**
	 * delete
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public  @ResponseBody
	Message delete(Long[] ids){
		if(ids.length >= userService.count()){
			return Message.error( "admin.common.deleteAllNotAllowed");
		}
		userService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}
