package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Community;
import com.homework.web.pojo.House;
import com.homework.web.pojo.Park;
import com.homework.web.pojo.Person;
import com.homework.web.pojo.Person_project;
import com.homework.web.pojo.Project;
import com.homework.web.pojo.Seat;
import com.homework.web.pojo.Unit;
import com.homework.web.pojo.vo.Person_projectVO;
import com.homework.web.service.impl.CommunityServiceImpl;
import com.homework.web.service.impl.HouseServiceImpl;
import com.homework.web.service.impl.ParkServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.Person_projectServiceImpl;
import com.homework.web.service.impl.ProjectServiceImpl;
import com.homework.web.service.impl.SeatServiceImpl;
import com.homework.web.service.impl.UnitServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/person_project")
@Api(tags = "路径共同前缀：/person_project。主要针对缴费记录表", description = "Person_projectController")
public class Person_projectController {
	@Autowired
	Person_projectServiceImpl person_projectServiceImpl;
	@Autowired
	ProjectServiceImpl projectServiceImpl;
	@Autowired
	CommunityServiceImpl communityServiceImpl;
	@Autowired
	UnitServiceImpl unitServiceImpl;
	@Autowired
	HouseServiceImpl houseServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;
	@Autowired
	ParkServiceImpl parkServiceImpl;
	@Autowired
	SeatServiceImpl seatServiceImpl;

	@GetMapping("/main")
	@ApiOperation("财务管理-缴费清单：主页")
	public String main(Model model) {
		return "menu/person_project::main";
	}

	@GetMapping("/insert")
	@ApiOperation("财务管理-缴费清单：获取添加缴费清单的弹出框")
	public String insert(Model model) {
		List<Project> projectList = projectServiceImpl.selectAll();
		model.addAttribute("projectList", projectList);
		return "menu/person_project::insert";
	}

	@GetMapping("/select_div")
	@ApiOperation("财务管理-缴费清单：表格")
	public String select_div(Model model, String name, String is_handle, String person_name, String data_name) {
		List<Person_project> person_projectList = new ArrayList<Person_project>();
		if (name.equals("")) {
			person_projectList = person_projectServiceImpl.selectAll();
		} else {
			List<Project> projectList = projectServiceImpl.selectByName(name);
			for (Project project : projectList) {
				List<Person_project> person_projectList2 = person_projectServiceImpl
						.selectByProject_id(project.getId());
				person_projectList.addAll(person_projectList2);
			}
		}
		List<Person_project> person_projectList3 = new ArrayList<Person_project>();
		if (is_handle.equals("true")) {
			for (Person_project person_project : person_projectList) {
				if (person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		} else if (is_handle.equals("false")) {
			for (Person_project person_project : person_projectList) {
				if (!person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		}
		if (!person_name.equals("")) {
			ArrayList<Person_project> arrayList = new ArrayList<Person_project>();
			for (Person_project person_project : person_projectList) {
				Person person = personServiceImpl.selectById(person_project.getPerson_id());
				if (person.getName().contains(person_name)) {
					arrayList.add(person_project);
				}
			}
			person_projectList = arrayList;
		}
		ArrayList<Person_projectVO> person_projectList2 = new ArrayList<Person_projectVO>();
		for (Person_project person_project : person_projectList) {
			Person_projectVO person_projectVO = new Person_projectVO();
			BeanUtils.copyProperties(person_project, person_projectVO);// 复制同名属性
			Person person = personServiceImpl.selectById(person_project.getPerson_id());
			Project project = projectServiceImpl.selectById(person_project.getProject_id());
			String data = "";
			Boolean type = person_project.getType();
			if (type != null) {
				Integer type_id = person_project.getType_id();
				if (type) {
					House house = houseServiceImpl.selectById(type_id);
					Unit unit = unitServiceImpl.selectById(house.getUnit_id());
					Community community = communityServiceImpl.selectById(unit.getCommunity_id());
					data = "" + community.getCode() + "号楼栋" + unit.getCode() + "号单元" + house.getCode() + "号房屋";
				} else {
					Seat seat = seatServiceImpl.selectById(type_id);
					Park park = parkServiceImpl.selectById(seat.getPark_id()).get(0);
					data = "" + park.getType() + park.getCode() + "号" + seat.getRegion() + "分区" + seat.getCode()
							+ "号车位";
				}
			}
			person_projectVO.setData(data);
			person_projectVO.setPerson_name(person.getName());
			person_projectVO.setProject_name(project.getName());
			person_projectList2.add(person_projectVO);
		}
		if (!data_name.equals("")) {
			ArrayList<Person_projectVO> person_projectList4 = new ArrayList<Person_projectVO>();
			for (Person_projectVO person_projectVO : person_projectList2) {
				if (person_projectVO.getData().contains(data_name)) {
					person_projectList4.add(person_projectVO);
				}
			}
			person_projectList2 = person_projectList4;
		}
		model.addAttribute("person_projectList", person_projectList2);
		return "menu/person_project::select_div";
	}

	@GetMapping("/select_unit")
	@ApiOperation("财务管理-缴费清单：新增缴费清单的弹出框的局部刷新")
	public String select_unit(Model model, String community_id) {
		community_id = community_id.trim();
		List<Unit> unitList = new ArrayList<Unit>();
		if (community_id.equals("")) {
			unitList = unitServiceImpl.selectAll();
		} else {
			unitList = unitServiceImpl.selectByCommunity_id(Integer.parseInt(community_id));
		}
		model.addAttribute("unitList", unitList);
		return "menu/person_project::select_unit";
	}

	@GetMapping("/select_unit2")
	@ApiOperation("财务管理-缴费清单：新增缴费清单的弹出框的局部刷新")
	public String select_unit2(Model model, String community_id) {
		community_id = community_id.trim();
		List<Unit> unitList = new ArrayList<Unit>();
		if (community_id.equals("")) {
			unitList = unitServiceImpl.selectAll();
		} else {
			unitList = unitServiceImpl.selectByCommunity_id(Integer.parseInt(community_id));
		}
		model.addAttribute("unitList", unitList);
		return "menu/person_project::select_unit2";
	}

	@GetMapping("/select_house")
	@ApiOperation("财务管理-缴费清单：新增缴费清单的弹出框的局部刷新")
	public String select_house(Model model, String unit_id) {
		unit_id = unit_id.trim();
		List<House> houseList = new ArrayList<House>();
		if (unit_id.equals("")) {
			houseList = houseServiceImpl.selectAll();
		} else {
			houseList = houseServiceImpl.selectByUnit_id(Integer.parseInt(unit_id));
		}
		model.addAttribute("houseList", houseList);
		return "menu/person_project::select_house";
	}

	@GetMapping("/select_seat")
	@ApiOperation("财务管理-缴费清单：新增缴费清单的弹出框的局部刷新")
	public String select_park(Model model, String park_id) {
		List<Seat> seatList = seatServiceImpl.selectByPark_idPerson_idIsNotNull(Integer.parseInt(park_id));
		model.addAttribute("seatList", seatList);
		return "menu/person_project::select_seat";
	}

	@GetMapping("/range")
	@ApiOperation("财务管理-缴费清单：新增缴费清单的弹出框的局部刷新")
	public String range(Model model, String range) {
		if (range.equals("小区")) {
			return "menu/person_project::select_range";
		} else if (range.equals("楼栋")) {
			List<Community> communityList = communityServiceImpl.selectAll();
			model.addAttribute("communityList", communityList);
			return "menu/person_project::select_community1";
		} else if (range.equals("单元")) {
			List<Community> communityList = communityServiceImpl.selectAll();
			model.addAttribute("communityList", communityList);
			return "menu/person_project::select_community2";
		} else if (range.equals("房屋")) {
			List<Community> communityList = communityServiceImpl.selectAll();
			model.addAttribute("communityList", communityList);
			return "menu/person_project::select_community3";
		} else if (range.equals("所有停车场")) {
			return "menu/person_project::select_range";
		} else if (range.equals("一个停车场")) {
			List<Park> parkList = parkServiceImpl.selectAll();
			model.addAttribute("parkList", parkList);
			return "menu/person_project::select_park1";
		} else if (range.equals("一个车位")) {
			List<Park> parkList = parkServiceImpl.selectAll();
			model.addAttribute("parkList", parkList);
			return "menu/person_project::select_park2";
		}
		return "menu/person_project::select_range";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("财务管理-缴费清单：进行一次收费操作")
	public HashMap<String, String> post(String range, String community_id, String unit_id, String house_id,
			String project_id, String park_id, String seat_id) {
		if (project_id.equals("")) {
			throw new RuntimeException("收费项目不可为空");
		}
		Project project = projectServiceImpl.selectById(Integer.parseInt(project_id));
		if (range.equals("小区")) {
			// 查出所有楼栋，所有单元，所有房屋的所有业主，向其收费
			List<Community> communityList = communityServiceImpl.selectAll();
			for (Community community : communityList) {
				List<Unit> unitList = unitServiceImpl.selectByCommunity_id(community.getId());
				for (Unit unit : unitList) {
					List<House> houseList = houseServiceImpl.selectByUnit_id(unit.getId());
					for (House house : houseList) {
						if (house.getPerson_id() != null) {
							Person person = personServiceImpl.selectById(house.getPerson_id());
							if (person != null) {
								Person_project person_project = new Person_project();
								person_project.setCreate_time(new Date());
								person_project.setIs_handle(false);
								person_project.setPerson_id(person.getId());
								person_project.setProject_id(Integer.parseInt(project_id));
								person_project.setType(true);
								person_project.setType_id(house.getId());
								if (project.getMoney_type()) {
									person_project.setMoney(project.getMoney());
								} else {
									person_project.setMoney(project.getMoney() * house.getArea());
								}
								person_projectServiceImpl.insert(person_project);
							}
						}
					}
				}
			}
		} else if (range.equals("楼栋")) {
			// 查出该楼栋的所有单元，所有房屋的所有业主，向其收费
			if (community_id.equals("")) {
				throw new RuntimeException("楼栋不可为空");
			} else {
				List<Unit> unitList = unitServiceImpl.selectByCommunity_id(Integer.parseInt(community_id));
				for (Unit unit : unitList) {
					List<House> houseList = houseServiceImpl.selectByUnit_id(unit.getId());
					for (House house : houseList) {
						if (house.getPerson_id() != null) {
							Person person = personServiceImpl.selectById(house.getPerson_id());
							if (person != null) {
								Person_project person_project = new Person_project();
								person_project.setCreate_time(new Date());
								person_project.setIs_handle(false);
								person_project.setPerson_id(person.getId());
								person_project.setProject_id(Integer.parseInt(project_id));
								person_project.setType(true);
								person_project.setType_id(house.getId());
								if (project.getMoney_type()) {
									person_project.setMoney(project.getMoney());
								} else {
									person_project.setMoney(project.getMoney() * house.getArea());
								}
								person_projectServiceImpl.insert(person_project);
							}
						}
					}
				}
			}
		} else if (range.equals("单元")) {
			// 查出该单元的所有房屋的所有业主，向其收费
			if (unit_id.equals("")) {
				throw new RuntimeException("单元不可为空");
			} else {
				List<House> houseList = houseServiceImpl.selectByUnit_id(Integer.parseInt(unit_id));
				for (House house : houseList) {
					if (house.getPerson_id() != null) {
						Person person = personServiceImpl.selectById(house.getPerson_id());
						if (person != null) {
							Person_project person_project = new Person_project();
							person_project.setCreate_time(new Date());
							person_project.setIs_handle(false);
							person_project.setPerson_id(person.getId());
							person_project.setProject_id(Integer.parseInt(project_id));
							person_project.setType(true);
							person_project.setType_id(house.getId());
							if (project.getMoney_type()) {
								person_project.setMoney(project.getMoney());
							} else {
								person_project.setMoney(project.getMoney() * house.getArea());
							}
							person_projectServiceImpl.insert(person_project);
						}
					}
				}
			}
		} else if (range.equals("房屋")) {
			// 查出该房屋的业主，向其收费
			if (house_id.equals("")) {
				throw new RuntimeException("房屋不可为空");
			} else {
				House house = houseServiceImpl.selectById(Integer.parseInt(house_id));
				if (house.getPerson_id() != null) {
					Person person = personServiceImpl.selectById(house.getPerson_id());
					if (person != null) {
						Person_project person_project = new Person_project();
						person_project.setCreate_time(new Date());
						person_project.setIs_handle(false);
						person_project.setPerson_id(person.getId());
						person_project.setProject_id(Integer.parseInt(project_id));
						person_project.setType(true);
						person_project.setType_id(house.getId());
						if (project.getMoney_type()) {
							person_project.setMoney(project.getMoney());
						} else {
							person_project.setMoney(project.getMoney() * house.getArea());
						}
						person_projectServiceImpl.insert(person_project);
					}
				}
			}
		} else if (range.equals("所有停车场")) {
			List<Park> parkList = parkServiceImpl.selectAll();
			for (Park park : parkList) {
				List<Seat> seatList = seatServiceImpl.selectByPark_idPerson_idIsNotNull(park.getId());
				for (Seat seat : seatList) {
					Person person = personServiceImpl.selectById(seat.getPerson_id());
					if (person != null) {
						Person_project person_project = new Person_project();
						person_project.setCreate_time(new Date());
						person_project.setIs_handle(false);
						person_project.setPerson_id(person.getId());
						person_project.setProject_id(Integer.parseInt(project_id));
						person_project.setType(false);
						person_project.setType_id(seat.getId());
						if (project.getMoney_type()) {
							person_project.setMoney(project.getMoney());
						} else {
							person_project.setMoney(project.getMoney() * seat.getArea());
						}
						person_projectServiceImpl.insert(person_project);
					}
				}
			}
		} else if (range.equals("一个停车场")) {
			if (park_id.equals("")) {
				throw new RuntimeException("停车场不可为空");
			}
			List<Park> parkList = parkServiceImpl.selectById(Integer.parseInt(park_id));
			List<Seat> seatList = seatServiceImpl.selectByPark_idPerson_idIsNotNull(parkList.get(0).getId());
			for (Seat seat : seatList) {
				Person person = personServiceImpl.selectById(seat.getPerson_id());
				if (person != null) {
					Person_project person_project = new Person_project();
					person_project.setCreate_time(new Date());
					person_project.setIs_handle(false);
					person_project.setPerson_id(person.getId());
					person_project.setProject_id(Integer.parseInt(project_id));
					person_project.setType(false);
					person_project.setType_id(seat.getId());
					if (project.getMoney_type()) {
						person_project.setMoney(project.getMoney());
					} else {
						person_project.setMoney(project.getMoney() * seat.getArea());
					}
					person_projectServiceImpl.insert(person_project);
				}
			}
		} else if (range.equals("一个车位")) {
			if (seat_id.equals("")) {
				throw new RuntimeException("车位不可为空");
			}
			Seat seat = seatServiceImpl.selectById(Integer.parseInt(seat_id));
			if (seat != null) {
				Person person = personServiceImpl.selectById(seat.getPerson_id());
				if (person != null) {
					Person_project person_project = new Person_project();
					person_project.setCreate_time(new Date());
					person_project.setIs_handle(false);
					person_project.setPerson_id(person.getId());
					person_project.setProject_id(Integer.parseInt(project_id));
					person_project.setType(false);
					person_project.setType_id(seat.getId());
					if (project.getMoney_type()) {
						person_project.setMoney(project.getMoney());
					} else {
						person_project.setMoney(project.getMoney() * seat.getArea());
					}
					person_projectServiceImpl.insert(person_project);
				}
			}
		}
		return new HashMap<String, String>();
	}

	@GetMapping("/person/main")
	@ApiOperation("财务管理-缴费清单（业主）：主页")
	public String person_main(Model model) {
		return "menu/person_project_person::main";
	}

	@GetMapping("/person/select_div")
	@ApiOperation("财务管理-缴费清单（业主）：表格")
	public String person_select_div(Model model, String is_handle) {
		List<Person_project> person_projectList = new ArrayList<Person_project>();
		// 获取业主信息
		Person person = (Person) SecurityUtils.getSubject().getPrincipal();
		if (is_handle.equals("true")) {
			person_projectList = person_projectServiceImpl.selectByPerson_idIs_handle(person.getId(), true);
		} else if (is_handle.equals("false")) {
			person_projectList = person_projectServiceImpl.selectByPerson_idIs_handle(person.getId(), false);
		} else {
			person_projectList = person_projectServiceImpl.selectByPerson_id(person.getId());
		}
		ArrayList<Person_projectVO> person_projectList2 = new ArrayList<Person_projectVO>();
		for (Person_project person_project : person_projectList) {
			Person_projectVO person_projectVO = new Person_projectVO();
			BeanUtils.copyProperties(person_project, person_projectVO);// 复制同名属性
			Project project = projectServiceImpl.selectById(person_project.getProject_id());
			person_projectVO.setPerson_name(personServiceImpl.selectById(person_project.getPerson_id()).getName());
			person_projectVO.setProject_name(project.getName());
			person_projectList2.add(person_projectVO);
		}
		model.addAttribute("person_projectList", person_projectList2);
		return "menu/person_project_person::select_div";
	}

	@PostMapping("/person/{id:[0-9]+}/handle/{method}")
	@ResponseBody
	@ApiOperation("财务管理-缴费清单：缴费")
	public HashMap<String, String> handle(@PathVariable Integer id, @PathVariable String method) {
		Person_project person_project = person_projectServiceImpl.selectById(id);
		Project project = projectServiceImpl.selectById(person_project.getProject_id());
		Date date = new Date();
		if (project.getStart_date().getTime() > date.getTime()) {
			throw new RuntimeException("现在还未到开始缴费时间");
		}
		if (project.getEnd_date().getTime() < date.getTime()) {
			throw new RuntimeException("现在已经超过缴费截止时间");
		}
		if (person_project.getIs_handle()) {
			throw new RuntimeException("已经缴费过了");
		} else {
			person_project.setIs_handle(true);
			person_project.setMethod(method);
			person_project.setEnd_time(new Date());
			person_projectServiceImpl.update(person_project);
		}
		return new HashMap<String, String>();
	}

	@PostMapping("/person/{id:[0-9]+}/unhandle")
	@ResponseBody
	@ApiOperation("财务管理-缴费清单：取消缴费")
	public HashMap<String, String> unhandle(@PathVariable Integer id) {
		Person_project person_project = person_projectServiceImpl.selectById(id);
		if (!person_project.getIs_handle()) {
			throw new RuntimeException("还未缴费呢");
		} else {
			person_project.setIs_handle(false);
			person_project.setEnd_time(null);
			person_project.setMethod(null);
			person_projectServiceImpl.update(person_project);
		}
		return new HashMap<String, String>();
	}

	@PostMapping("/water_fee")
	@ResponseBody
	@ApiOperation("档案管理-房屋管理：进行一次收水费的操作")
	public Person_project water_fee(String house_id, String project_id, String quantity/* , String price */) {
		System.out.println(quantity);
//		System.out.println(price);
		if (project_id.equals("0")) {
			throw new RuntimeException("收费项目不可为空");
		}
		House house = houseServiceImpl.selectById(Integer.parseInt(house_id));
		if (house.getPerson_id() == null) {
			throw new RuntimeException("该房屋无业主，无法收水费");
		}
		if (quantity.equals("")) {
			throw new RuntimeException("用水量不可为空");
		}
//		if (price.equals("")) {
//			throw new RuntimeException("水的单价（元/吨）不可为空");
//		}
		Project project = projectServiceImpl.selectById(Integer.parseInt(project_id));
		Person_project person_project = new Person_project();
		person_project.setCreate_time(new Date());
		person_project.setIs_handle(false);
		person_project.setPerson_id(house.getPerson_id());
		person_project.setProject_id(Integer.parseInt(project_id));
		person_project.setType(true);
		person_project.setType_id(house.getId());
		if (project.getMoney_type()) {
			person_project.setMoney(project.getMoney());
		} else {
			person_project.setMoney(Double.parseDouble(quantity) * project.getMoney());
		}
		return person_projectServiceImpl.insert(person_project);
	}

	@GetMapping("/water_fee/main")
	@ApiOperation("财务管理-水费：主页")
	public String water_fee_main() {
		return "menu/water_fee::main";
	}

	@GetMapping("/water_fee/select_div")
	@ApiOperation("财务管理-水费：表格")
	public String water_fee_select_div(String is_handle, Model model, String data_name) {
		String name = "水费";
		List<Person_project> person_projectList = new ArrayList<Person_project>();
		List<Project> projectList = projectServiceImpl.selectByName(name);
		for (Project project : projectList) {
			List<Person_project> person_projectList2 = person_projectServiceImpl.selectByProject_id(project.getId());
			person_projectList.addAll(person_projectList2);
		}
		List<Person_project> person_projectList3 = new ArrayList<Person_project>();
		if (is_handle.equals("true")) {
			for (Person_project person_project : person_projectList) {
				if (person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		} else if (is_handle.equals("false")) {
			for (Person_project person_project : person_projectList) {
				if (!person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		}
		ArrayList<Person_projectVO> person_projectList2 = new ArrayList<Person_projectVO>();
		for (Person_project person_project : person_projectList) {
			Person_projectVO person_projectVO = new Person_projectVO();
			BeanUtils.copyProperties(person_project, person_projectVO);
			Person person = personServiceImpl.selectById(person_project.getPerson_id());
			Project project = projectServiceImpl.selectById(person_project.getProject_id());
			String data = "";
			Boolean type = person_project.getType();
			if (type != null) {
				Integer type_id = person_project.getType_id();
				if (type) {
					House house = houseServiceImpl.selectById(type_id);
					Unit unit = unitServiceImpl.selectById(house.getUnit_id());
					Community community = communityServiceImpl.selectById(unit.getCommunity_id());
					data = "" + community.getCode() + "号楼栋" + unit.getCode() + "号单元" + house.getCode() + "号房屋";
				} else {
					Seat seat = seatServiceImpl.selectById(type_id);
					Park park = parkServiceImpl.selectById(seat.getPark_id()).get(0);
					data = "" + park.getType() + park.getCode() + "号" + seat.getRegion() + "分区" + seat.getCode()
							+ "号车位";
				}
			}
			person_projectVO.setData(data);
			person_projectVO.setPerson_name(person.getName());
			person_projectVO.setProject_name(project.getName());
			person_projectList2.add(person_projectVO);
		}
		if (!data_name.equals("")) {
			ArrayList<Person_projectVO> person_projectList4 = new ArrayList<Person_projectVO>();
			for (Person_projectVO person_projectVO : person_projectList2) {
				if (person_projectVO.getData().contains(data_name)) {
					person_projectList4.add(person_projectVO);
				}
			}
			person_projectList2 = person_projectList4;
		}
		model.addAttribute("person_projectList", person_projectList2);
		return "menu/water_fee::select_div";
	}

	@GetMapping("/manage_fee/main")
	@ApiOperation("财务管理-物业费：主页")
	public String manager_fee_main() {
		return "menu/manage_fee::main";
	}

	@GetMapping("/manage_fee/select_div")
	@ApiOperation("财务管理-物业费：表格")
	public String manage_fee_select_div(String is_handle, Model model, String name, String data_name) {
		String name3 = "物业费";
		List<Person_project> person_projectList = new ArrayList<Person_project>();
		List<Project> projectList = projectServiceImpl.selectByName(name3);
		for (Project project : projectList) {
			List<Person_project> person_projectList2 = person_projectServiceImpl.selectByProject_id(project.getId());
			person_projectList.addAll(person_projectList2);
		}
		List<Person_project> person_projectList3 = new ArrayList<Person_project>();
		if (is_handle.equals("true")) {
			for (Person_project person_project : person_projectList) {
				if (person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		} else if (is_handle.equals("false")) {
			for (Person_project person_project : person_projectList) {
				if (!person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		}
		if (!name.equals("")) {
			ArrayList<Person_project> arrayList = new ArrayList<Person_project>();
			for (Person_project person_project : person_projectList) {
				Project project = projectServiceImpl.selectById(person_project.getProject_id());
				if (project.getName().contains(name)) {
					arrayList.add(person_project);
				}
			}
			person_projectList = arrayList;
		}
		ArrayList<Person_projectVO> person_projectList2 = new ArrayList<Person_projectVO>();
		for (Person_project person_project : person_projectList) {
			Person_projectVO person_projectVO = new Person_projectVO();
			BeanUtils.copyProperties(person_project, person_projectVO);// 复制同名属性
			Person person = personServiceImpl.selectById(person_project.getPerson_id());
			Project project = projectServiceImpl.selectById(person_project.getProject_id());
			String data = "";
			Boolean type = person_project.getType();
			if (type != null) {
				Integer type_id = person_project.getType_id();
				if (type) {
					House house = houseServiceImpl.selectById(type_id);
					Unit unit = unitServiceImpl.selectById(house.getUnit_id());
					Community community = communityServiceImpl.selectById(unit.getCommunity_id());
					data = "" + community.getCode() + "号楼栋" + unit.getCode() + "号单元" + house.getCode() + "号房屋";
				} else {
					Seat seat = seatServiceImpl.selectById(type_id);
					Park park = parkServiceImpl.selectById(seat.getPark_id()).get(0);
					data = "" + park.getType() + park.getCode() + "号" + seat.getRegion() + "分区" + seat.getCode()
							+ "号车位";
				}
			}
			person_projectVO.setData(data);
			person_projectVO.setPerson_name(person.getName());
			person_projectVO.setProject_name(project.getName());
			person_projectList2.add(person_projectVO);
		}
		if (!data_name.equals("")) {
			ArrayList<Person_projectVO> person_projectList4 = new ArrayList<Person_projectVO>();
			for (Person_projectVO person_projectVO : person_projectList2) {
				if (person_projectVO.getData().contains(data_name)) {
					person_projectList4.add(person_projectVO);
				}
			}
			person_projectList2 = person_projectList4;
		}
		model.addAttribute("person_projectList", person_projectList2);
		return "menu/manage_fee::select_div";
	}

	@GetMapping("/seat_fee/main")
	@ApiOperation("财务管理-车位费：主页")
	public String seat_fee_main() {
		return "menu/seat_fee::main";
	}

	@GetMapping("/seat_fee/select_div")
	@ApiOperation("财务管理-车位费：表格")
	public String seat_fee_select_div(String is_handle, Model model, String name, String data_name) {
		String name3 = "车位费";
		List<Person_project> person_projectList = new ArrayList<Person_project>();
		List<Project> projectList = projectServiceImpl.selectByName(name3);
		for (Project project : projectList) {
			List<Person_project> person_projectList2 = person_projectServiceImpl.selectByProject_id(project.getId());
			person_projectList.addAll(person_projectList2);
		}
		List<Person_project> person_projectList3 = new ArrayList<Person_project>();
		if (is_handle.equals("true")) {
			for (Person_project person_project : person_projectList) {
				if (person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		} else if (is_handle.equals("false")) {
			for (Person_project person_project : person_projectList) {
				if (!person_project.getIs_handle()) {
					person_projectList3.add(person_project);
				}
			}
			person_projectList = person_projectList3;
		}
		if (!name.equals("")) {
			ArrayList<Person_project> arrayList = new ArrayList<Person_project>();
			for (Person_project person_project : person_projectList) {
				Project project = projectServiceImpl.selectById(person_project.getProject_id());
				if (project.getName().contains(name)) {
					arrayList.add(person_project);
				}
			}
			person_projectList = arrayList;
		}
		ArrayList<Person_projectVO> person_projectList2 = new ArrayList<Person_projectVO>();
		for (Person_project person_project : person_projectList) {
			Person_projectVO person_projectVO = new Person_projectVO();
			BeanUtils.copyProperties(person_project, person_projectVO);// 复制同名属性
			Person person = personServiceImpl.selectById(person_project.getPerson_id());
			Project project = projectServiceImpl.selectById(person_project.getProject_id());
			String data = "";
			Boolean type = person_project.getType();
			if (type != null) {
				Integer type_id = person_project.getType_id();
				if (type) {
					House house = houseServiceImpl.selectById(type_id);
					Unit unit = unitServiceImpl.selectById(house.getUnit_id());
					Community community = communityServiceImpl.selectById(unit.getCommunity_id());
					data = "" + community.getCode() + "号楼栋" + unit.getCode() + "号单元" + house.getCode() + "号房屋";
				} else {
					Seat seat = seatServiceImpl.selectById(type_id);
					Park park = parkServiceImpl.selectById(seat.getPark_id()).get(0);
					data = "" + park.getType() + park.getCode() + "号" + seat.getRegion() + "分区" + seat.getCode()
							+ "号车位";
				}
			}
			person_projectVO.setData(data);
			person_projectVO.setPerson_name(person.getName());
			person_projectVO.setProject_name(project.getName());
			person_projectList2.add(person_projectVO);
		}
		if (!data_name.equals("")) {
			ArrayList<Person_projectVO> person_projectList4 = new ArrayList<Person_projectVO>();
			for (Person_projectVO person_projectVO : person_projectList2) {
				if (person_projectVO.getData().contains(data_name)) {
					person_projectList4.add(person_projectVO);
				}
			}
			person_projectList2 = person_projectList4;
		}
		model.addAttribute("person_projectList", person_projectList2);
		return "menu/seat_fee::select_div";
	}

	@GetMapping("/all_fee/main")
	@ApiOperation("财务管理-收费总览：主页")
	public String all_fee_main(Model model) {
//		LinkedHashMap<Project, Double[]> projectStringArrayMap = new LinkedHashMap<Project, Double[]>();
//		List<Project> projectList = projectServiceImpl.selectAll();
//		for (Project project : projectList) {
//			List<Person_project> person_projectList = person_projectServiceImpl.selectByProject_id(project.getId());
//			double money_handle = 0;
//			double money_all = 0;
//			for (Person_project person_project : person_projectList) {
//				money_all += person_project.getMoney();
//				if (person_project.getIs_handle()) {
//					money_handle += person_project.getMoney();
//				}
//			}
//			Double[] moneys = new Double[] { money_handle, money_all };
//			projectStringArrayMap.put(project, moneys);
//		}
//		model.addAttribute("projectStringArrayMap", projectStringArrayMap);
		//
		// 取出所有项目
		List<Project> projectList = projectServiceImpl.selectAll();
		Double m1 = 0.0;
		Double m2 = 0.0;
		Double m3 = 0.0;
		Double m4 = 0.0;
		Double m5 = 0.0;
		Double m6 = 0.0;
		for (Project project : projectList) {
			if (project.getName().contains("物业费")) {
				List<Person_project> person_projectList = person_projectServiceImpl.selectByProject_id(project.getId());
				for (Person_project person_project : person_projectList) {
					m2 += person_project.getMoney();
					if (person_project.getIs_handle()) {
						m1 += person_project.getMoney();
					}
				}
			}
			if (project.getName().contains("水费")) {
				List<Person_project> person_projectList = person_projectServiceImpl.selectByProject_id(project.getId());
				for (Person_project person_project : person_projectList) {
					m4 += person_project.getMoney();
					if (person_project.getIs_handle()) {
						m3 += person_project.getMoney();
					}
				}
			}
			if (project.getName().contains("车位费")) {
				List<Person_project> person_projectList = person_projectServiceImpl.selectByProject_id(project.getId());
				for (Person_project person_project : person_projectList) {
					m6 += person_project.getMoney();
					if (person_project.getIs_handle()) {
						m5 += person_project.getMoney();
					}
				}
			}
		}
		ArrayList<Object[]> dataList = new ArrayList<Object[]>();
		dataList.add(new Object[] { "物业费", m1 + "", m2 + "", 1 });
		dataList.add(new Object[] { "水费", m3 + "", m4 + "", 2 });
		dataList.add(new Object[] { "车位费", m5 + "", m6 + "", 3 });
		model.addAttribute("dataList", dataList);
		return "menu/all_fee::main";
	}

	@GetMapping("/all_fee/select_div/{id:[0-9]+}")
	@ApiOperation("财务管理-收费总览：标题实现跳转")
	public String all_fee_select_div(@PathVariable Integer id, Model model, String name, String is_handle) {
		String data2 = "物业费";
		if (id == 1) {
			data2 = "物业费";
		} else if (id == 2) {
			data2 = "水费";
		} else if (id == 3) {
			data2 = "车位费";
		}
		System.out.println(data2);
		// 取出所有项目
		List<Project> selectAll = projectServiceImpl.selectAll();
		List<Project> selectAll2 = new ArrayList<Project>();
		// 符合要求的项目类型保存下来
		for (Project project : selectAll) {
			if (project.getName().contains(data2)) {
				selectAll2.add(project);
			}
		}
		System.out.println(selectAll.size());
		System.out.println(selectAll2.size());
		System.out.println("---------");
		List<Project> selectAll3 = new ArrayList<Project>();
		if (name != null) {
			for (Project project : selectAll2) {
				if (!name.equals("")) {
					if (project.getName().contains(name)) {
						selectAll3.add(project);
					}
				} else {
					selectAll3.add(project);
				}
			}
		} else {
			selectAll3 = selectAll2;
		}
		System.out.println(selectAll3.size());
		List<Person_project> person_projectList = new ArrayList<Person_project>();
		for (Project project : selectAll3) {
			person_projectList.addAll(person_projectServiceImpl.selectByProject_id(project.getId()));
		}
		List<Person_project> person_projectList3 = new ArrayList<Person_project>();
		if (is_handle != null) {
			if (is_handle.equals("true")) {
				for (Person_project person_project : person_projectList) {
					if (person_project.getIs_handle() == true) {
						person_projectList3.add(person_project);
					}
				}
				person_projectList = person_projectList3;
			} else if (is_handle.equals("false")) {
				for (Person_project person_project : person_projectList) {
					if (person_project.getIs_handle() == false) {
						person_projectList3.add(person_project);
					}
				}
				person_projectList = person_projectList3;
			}
		} 
		System.out.println(person_projectList.size());
		// 转化为合适的类型
		ArrayList<Person_projectVO> person_projectList2 = new ArrayList<Person_projectVO>();
		for (Person_project person_project : person_projectList) {
			Person_projectVO person_projectVO = new Person_projectVO();
			BeanUtils.copyProperties(person_project, person_projectVO);// 复制同名属性
			Person person = personServiceImpl.selectById(person_project.getPerson_id());
			Project project = projectServiceImpl.selectById(person_project.getProject_id());
			String data = "";
			Boolean type = person_project.getType();
			if (type != null) {
				Integer type_id = person_project.getType_id();
				if (type) {
					House house = houseServiceImpl.selectById(type_id);
					Unit unit = unitServiceImpl.selectById(house.getUnit_id());
					Community community = communityServiceImpl.selectById(unit.getCommunity_id());
					data = "" + community.getCode() + "号楼栋" + unit.getCode() + "号单元" + house.getCode() + "号房屋";
				} else {
					Seat seat = seatServiceImpl.selectById(type_id);
					Park park = parkServiceImpl.selectById(seat.getPark_id()).get(0);
					data = "" + park.getType() + park.getCode() + "号" + seat.getRegion() + "分区" + seat.getCode()
							+ "号车位";
				}
			}
			person_projectVO.setData(data);
			person_projectVO.setPerson_name(person.getName());
			person_projectVO.setProject_name(project.getName());
			person_projectList2.add(person_projectVO);
		}
		model.addAttribute("person_projectList", person_projectList2);
		model.addAttribute("id", id);
		return "menu/all_fee::select_div";
	}

}
