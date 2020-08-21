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
import com.homework.web.pojo.Role;
import com.homework.web.pojo.Role_permission;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.PermissionServiceImpl;
import com.homework.web.service.impl.RoleServiceImpl;
import com.homework.web.service.impl.Role_permissionServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;
//用户认证和授权对应的Realm
public class UserShiroRealm extends AuthorizingRealm {

	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	RoleServiceImpl roleServiceImpl;
	@Autowired
	PermissionServiceImpl permissionServiceImpl;
	@Autowired
	Role_permissionServiceImpl role_permissionServiceImpl;

	// 认证（用户名和密码）
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		User user = userServiceImpl.selectByUsername(username);
		if (user != null) {
			// 获取加密解密器
			HashedCredentialsMatcher hashedCredentialsMatcher = (HashedCredentialsMatcher) getCredentialsMatcher();
			// 对密码进行加密（合理的情况下应该是在插入数据时进行加密的，但是为了数据库密码可观看，便不对数据库密码进行加密，而是在登录时进行加密）
			Object password = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(), user.getPassword(), null,
					hashedCredentialsMatcher.getHashIterations());// 加密后的密码
			return new SimpleAuthenticationInfo(user, password, getName());
		} else {
			return null;
		}
	}

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = (User) principals.getPrimaryPrincipal();
		// 添加角色
		Role role = roleServiceImpl.selectById(user.getRole_id());
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
