package com.homework.web.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Person;
import com.homework.web.pojo.Person_member;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.Person_memberServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/person_member")
@Api(tags = "路径共同前缀：/person_member。主要针对业主成员表", description = "Person_memberController")
public class Person_memberController {

	@Autowired
	PersonServiceImpl personServiceImpl;
	@Autowired
	Person_memberServiceImpl person_memberServiceImpl;

	@GetMapping("/main")
	@ApiOperation("档案管理-业主成员：主页")
	public String main(Model model) {
		List<Person> personList = personServiceImpl.selectAll();
		model.addAttribute("personList", personList);
		return "menu/person_member::main";
	}

	@GetMapping("/select_person/{person_id:[0-9]+}")
	@ApiOperation("档案管理-业主成员：通过选择框显示一个业主的信息")
	public String get(Model model, @PathVariable Integer person_id) {
		Person person = personServiceImpl.selectById(person_id);
		model.addAttribute("person", person);
		return "menu/person_member::select_person";
	}

	@GetMapping("/select_person")
	@ApiOperation("档案管理-业主成员：通过查询显示一个业主的信息")
	public String get2(Model model, String id_card_number, String phone) {
		Person person = null;
		if (id_card_number.equals("")) {
			if (!phone.equals("")) {
				List<Person> personList = personServiceImpl.selectByPhone(phone);
				if (personList.size() != 0) {
					person = personList.get(0);
				}
			}
		} else {
			if (phone.equals("")) {
				List<Person> personList = personServiceImpl.selectById_card_number(id_card_number);
				if (personList.size() != 0) {
					person = personList.get(0);
				}
			} else {
				List<Person> personList = personServiceImpl.selectById_card_numberPhone(id_card_number, phone);
				if (personList.size() != 0) {
					person = personList.get(0);
				}
			}
		}
		model.addAttribute("person", person);
		return "menu/person_member::select_person";
	}

	@GetMapping("/{person_id:[0-9]+}")
	@ApiOperation("档案管理-业主成员：一个业主的业主成员表格")
	public String p(Model model, @PathVariable Integer person_id) {
		List<Person_member> person_memberList = person_memberServiceImpl.selectByPerson_id(person_id);
		model.addAttribute("person_memberList", person_memberList);
		return "menu/person_member::select_div2";
	}

	@GetMapping("/insert/{person_id:[0-9]+}")
	@ApiOperation("档案管理-业主成员：一个业主的新增业主成员弹出框")
	public String pp(Model model, @PathVariable Integer person_id) {
		Person person = personServiceImpl.selectById(person_id);
		model.addAttribute("person", person);
		return "menu/person_member::insert";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("档案管理-业主成员：进行一次新增业主成员操作")
	public Person_member post(Person_member person_member) {
		System.out.println(person_member);
		if (person_memberServiceImpl.selectByPerson_idId_card_number(person_member.getPerson_id(),
				person_member.getId_card_number()) != null) {
			throw new RuntimeException("该业主已经存在同身份证的成员了");
		}
		if (person_memberServiceImpl.selectByPerson_idPhone(person_member.getPerson_id(),
				person_member.getPhone()) != null) {
			throw new RuntimeException("该业主已经存在同手机号的成员了");
		}
		person_member.setImage("user_default_image.jpg");
		return person_memberServiceImpl.insert(person_member);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("档案管理-业主成员：获取更新业主成员弹出框")
	public String update(Model model, @PathVariable Integer id) {
		Person_member person_member = person_memberServiceImpl.selectById(id);
		model.addAttribute("person_member", person_member);
		return "menu/person_member::update";
	}

	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("档案管理-业主成员：进行一次更新业主成员的操作")
	public Person_member updatePerson_member(Person_member person_member) {
		Person_member person_member2 = person_memberServiceImpl.selectById(person_member.getId());
		if (!person_member.getId_card_number().equals(person_member2.getId_card_number())) {
			if (person_memberServiceImpl.selectByPerson_idId_card_number(person_member.getPerson_id(),
					person_member.getId_card_number()) != null) {
				throw new RuntimeException("该业主下已存在同身份证的成员");
			}
		}
		if (!person_member.getPhone().equals(person_member2.getPhone())) {
			if (person_memberServiceImpl.selectByPerson_idPhone(person_member.getPerson_id(),
					person_member.getPhone()) != null) {
				throw new RuntimeException("该业主下已存在同手机号的成员");
			}
		}
		return person_memberServiceImpl.update(person_member);
	}

	@GetMapping("/delete/{id:[0-9]+}")
	@ResponseBody
	@ApiOperation("档案管理-业主成员：进行一次删除业主成员的操作")
	public HashMap<String, String> delete(Model model, @PathVariable Integer id) {
		try {
			person_memberServiceImpl.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("删除失败，请刷新页面后再尝试");
		}
		return new HashMap<String, String>();
	}
}
