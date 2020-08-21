package com.homework.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.homework.web.pojo.Permission;
import com.homework.web.pojo.Person;
import com.homework.web.pojo.Role;
import com.homework.web.pojo.Role_permission;
import com.homework.web.service.impl.PermissionServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.RoleServiceImpl;
import com.homework.web.service.impl.Role_permissionServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;
//业主认证和授权对应的Realm
public class PersonShiroRealm extends AuthorizingRealm {

	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	RoleServiceImpl roleServiceImpl;
	@Autowired
	PermissionServiceImpl permissionServiceImpl;
	@Autowired
	Role_permissionServiceImpl role_permissionServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;

	// 认证（手机和身份证）
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String phone = (String) token.getPrincipal();
		List<Person> personList = personServiceImpl.selectByPhone(phone);
		if (personList.size() == 0) {
			return null;
		} else {
			Person person = personList.get(0);
			// 获取加密解密器
			HashedCredentialsMatcher hashedCredentialsMatcher = (HashedCredentialsMatcher) getCredentialsMatcher();
			// 对密码进行加密（合理的情况下应该是在插入数据时进行加密的，但是为了数据库密码可观看，便不对数据库密码进行加密，而是在登录时进行加密）
			Object password = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(),
					person.getId_card_number(), null, hashedCredentialsMatcher.getHashIterations());// 加密后的密码
			return new SimpleAuthenticationInfo(person, password, getName());
		}
	}

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Person person = (Person) principals.getPrimaryPrincipal();
//		// 添加角色
		Role role = roleServiceImpl.selectById(person.getRole_id());
		authorizationInfo.addRole(role.getName());
		// 添加权限
		List<Role_permission> role_permissionList = role_permissionServiceImpl.selectByRole_id(role.getId());
		for (Role_permission role_permission : role_permissionList) {
			Permission permission = permissionServiceImpl.selectById(role_permission.getPermission_id());
			authorizationInfo.addStringPermission(permission.getName());
		}
		return authorizationInfo;
	}

}
