app.controller("indexController",function($scope,loginService){
	
	$scope.showName = function(){
		loginService.showName().success(function(response){
			$scope.loginName = response.username;
			$scope.cur_time = response.cur_time;
		});
	}
	
});