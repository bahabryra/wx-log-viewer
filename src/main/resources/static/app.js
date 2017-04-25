var globallObj = {};

var app = angular.module('stealer-log', ['angular-websocket'])
.factory('logData', $websocket => {
	var dataStream = $websocket('ws://localhost:8092/ws');
	
	var data = { dns : [], firewall : [], theTime: new Date() };
	
	function addDns(dns){
		var dnsLength = 12;
		data.dns.unshift(dns);
		
		if(data.dns.length > dnsLength){
			data.dns.splice(dnsLength, data.dns.length - dnsLength);
		}
	}
	
	function addFirewall(firewall){
		var firewallLength = 12;
		data.firewall.unshift(firewall);
		
		if(data.firewall.length > firewallLength){
			data.firewall.splice(firewallLength, data.firewall.length - firewallLength);
		}
	}
	
	dataStream.onMessage(event => {
		data.theTime = new Date();
		var obj = JSON.parse(event.data);
		
		if(obj.type === 'DNS'){
			addDns(obj);
			console.log('dns.length ' + data.dns.length);
		}else if(obj.type === 'FIREWALL') {
			addFirewall(obj);
			console.log('firewall.length ' + data.firewall.length);
		}else{
			console.log(obj);
			globallObj = obj;
			socket.close();
		}
	});
	
	return data;
})
.controller('logCtrl', ($scope, $interval, logData) => {
	$scope.data = logData;
	$scope.theTime = logData.theTime;
})
.filter('toDate', () => (epoch) => (new Date(epoch)).toLocaleString())
.filter('toTrClass', () => (destAddress) => {
	if("192.168.1.9" == destAddress){
		return "danger";
	}
	
	return "success";
});

