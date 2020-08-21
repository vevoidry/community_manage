package com.homework.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.homework.web.pojo.Person;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.AdviceServiceImpl;
import com.homework.web.service.impl.CommunityServiceImpl;
import com.homework.web.service.impl.HouseServiceImpl;
import com.homework.web.service.impl.MenuServiceImpl;
import com.homework.web.service.impl.ParkServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.Person_memberServiceImpl;
import com.homework.web.service.impl.Person_projectServiceImpl;
import com.homework.web.service.impl.ProjectServiceImpl;
import com.homework.web.service.impl.RepairServiceImpl;
import com.homework.web.service.impl.SeatServiceImpl;
import com.homework.web.service.impl.UnitServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "路径共同前缀：无。不针对任何一个表", description = "AppController")
public class AppController {

	@Autowired
	MenuServiceImpl menuServiceImpl;
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;
	@Autowired
	CommunityServiceImpl communityServiceImpl;
	@Autowired
	UnitServiceImpl unitServiceImpl;
	@Autowired
	HouseServiceImpl houseServiceImpl;
	@Autowired
	Person_memberServiceImpl person_memberServiceImpl;
	@Autowired
	ParkServiceImpl parkServiceImpl;
	@Autowired
	SeatServiceImpl seatServiceImpl;
	@Autowired
	ProjectServiceImpl projectServiceImpl;
	@Autowired
	Person_projectServiceImpl person_projectServiceImpl;
	@Autowired
	AdviceServiceImpl adviceServiceImpl;
	@Autowired
	RepairServiceImpl repairServiceImpl;

	@GetMapping("/")
	@ApiOperation("重定向：/login")
	public String index() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	@ApiOperation("跳转至登录页：login.html")
	public String login() {
		return "login";
	}

	@PostMapping("/doLogin")
	@ResponseBody
	@ApiOperation("进行登录")
	public HashMap<String, String> doLogin(@ApiParam("用户名/手机号") String username, String password, String role) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (username.equals("")) {
			throw new RuntimeException("用户名不可为空");
		}
		if (password.equals("")) {
			throw new RuntimeException("密码不可为空");
		}
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		if (role.equals("user")) {
			// 进行认证
			try {
				subject.login(token);
			} catch (AuthenticationException e) {
				throw new RuntimeException("用户名或密码错误，登录失败");
			}
			if (subject.isAuthenticated()) {
				// 把用户信息放入session
				User user = userServiceImpl.selectByUsername(username);
				subject.getSession().setAttribute("user", user);
				map.put("url", "/user");
			} else {
				throw new RuntimeException("用户名或密码错误，登录失败");
			}
		} else if (role.equals("person")) {
			// 进行认证
			try {
				subject.login(token);
			} catch (AuthenticationException e) {
				throw new RuntimeException("手机号或身份证错误，登录失败");
			}
			if (subject.isAuthenticated()) {
				// 把用户信息放入session
				List<Person> personList = personServiceImpl.selectByPhone(username);
				subject.getSession().setAttribute("person", personList.get(0));
				map.put("url", "/person");
			} else {
				throw new RuntimeException("手机号或身份证错误，登录失败");
			}
		} else {
			throw new RuntimeException("请选择登陆者类型");
		}
		return map;
	}

	@GetMapping("/doLogout")
	@ApiOperation("进行退出，退出后重定向到/")
	public String doLogout() {
		Subject subject = SecurityUtils.getSubject();
		// 清除sessoin
		subject.getSession().removeAttribute("user");
		subject.getSession().removeAttribute("person");
		subject.logout();
		// 重定向
		return "redirect:/";
	}


	// 主页
	@GetMapping("/main")
	@ApiOperation("系统管理-主页：主页")
	public String main(Model model) {
		// 楼栋数据
		Integer communitCount = communityServiceImpl.selectAll().size();
		model.addAttribute("communitCount", communitCount);
		// 单元数据
		int unitCount = unitServiceImpl.selectAll().size();
		model.addAttribute("unitCount", unitCount);
		// 房屋数据
		int houseCount = houseServiceImpl.selectAll().size();// 全部房屋
		int nullHouseCount = houseServiceImpl.selectByPerson_idIsNull().size();// 无主房屋
		model.addAttribute("houseCount", houseCount);
		model.addAttribute("nullHouseCount", nullHouseCount);
		// 业主数据
		int personCount = personServiceImpl.selectAll().size();
		model.addAttribute("personCount", personCount);
		// 停车场数据
		int parkCount = parkServiceImpl.selectAll().size();
		model.addAttribute("parkCount", parkCount);
		// 车位数据
		int seatCount = seatServiceImpl.selectAll().size();// 全部车位
		int nullSeatCount = seatServiceImpl.selectByPerson_idIsNull().size();// 无主车位
		model.addAttribute("seatCount", seatCount);
		model.addAttribute("nullSeatCount", nullSeatCount);
		// 收费项目数据
		int projectCount = projectServiceImpl.selectAll().size();
		model.addAttribute("projectCount", projectCount);
		// 收费记录数据
		int person_projectCount = person_projectServiceImpl.selectAll().size();// 全部
		int handledPerson_projectCount = person_projectServiceImpl.selectByIs_handle(true).size();// 已处理
		model.addAttribute("person_projectCount", person_projectCount);
		model.addAttribute("handledPerson_projectCount", handledPerson_projectCount);
		// 投诉建议数据
		int adviceCount = adviceServiceImpl.selectAll().size();// 全部
		int handledAdviceCount = adviceServiceImpl.selectByIs_handle(true).size();// 已处理
		model.addAttribute("adviceCount", adviceCount);
		model.addAttribute("handledAdviceCount", handledAdviceCount);
		// 维修数据
		int repairCount = repairServiceImpl.selectAll().size();// 全部
		int handledRepairCount = repairServiceImpl.selectByIs_handle(true).size();// 已处理
		model.addAttribute("repairCount", repairCount);
		model.addAttribute("handledRepairCount", handledRepairCount);
		return "menu/main::main";
	}

//	// 测试用
//	@GetMapping("/index")
//	public String index(Model model) {
//		LinkedHashMap<Menu, List<Menu>> menuMenuListMap = new LinkedHashMap<Menu, List<Menu>>();
//		// 获取所有父菜单
//		List<Menu> menuList = menuServiceImpl.selectByParent_idRank(0);
//		// 循环父菜单获取相应的子菜单
//		Iterator<Menu> iterator = menuList.iterator();
//		while (iterator.hasNext()) {
//			Menu menu = iterator.next();
//			List<Menu> menuList2 = menuServiceImpl.selectByParent_idRank(menu.getId());
//			menuMenuListMap.put(menu, menuList2);
//		}
//		model.addAttribute("menuMenuListMap", menuMenuListMap);
//		return "index";
//	}

	@PostMapping("/upload")
	@ResponseBody
	@ApiOperation("上传头像")
	public HashMap<String, String> upload(MultipartFile file) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		String name = file.getOriginalFilename();
		// 判断传输的文件名是否为空
		if (!name.equals("")) {
			String substring = name.substring(name.lastIndexOf("."));
			String real_name = UUID.randomUUID().toString() + substring;
			// 获取用于保存上传文件的文件夹路径
			String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/image/";
			File directory = new File(directoryPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			File file2 = new File(directory, real_name);
			if (!file2.exists()) {
				file2.createNewFile();
			}
			// 保存文件
			file.transferTo(file2);
			map.put("image_url", real_name);
		} else {
			throw new RuntimeException("请选择图片");
		}
		return map;
	}
}
