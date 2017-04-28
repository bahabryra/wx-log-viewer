var globallObj = {};

var app = angular.module('stealer-log', ['angular-websocket'])
.factory('logData', $websocket => {
	var dataStream = $websocket('ws://localhost:8092/ws');
	
	var data =	 { dns : []
				 , dnsIgnore: []
				 , dnsLength : 12
				 , firewall : []
				 , firewallIgnore : []
				 , firewallLength : 12
				 , theTime : new Date()
				};
		
	function addDns(dns){
		if(data.dnsIgnore.includes(dns.hostname)){
			console.log('dropping entry for dns. '+dns);
			return;
		}
		
		data.dns.unshift(dns);
		
		if(data.dns.length > data.dnsLength){
			data.dns.splice(data.dnsLength, data.dns.length - data.dnsLength);
		}
	}
	
	function addFirewall(firewall){
		if(data.firewallIgnore.includes(firewall.destinationAddress)){
			console.log('dropping entry for firewall. '+ firewall);
			return;
		}
		
		data.firewall.unshift(firewall);
		
		if(data.firewall.length > data.firewallLength){
			data.firewall.splice(data.firewallLength, data.firewall.length - data.firewallLength);
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
	
	$scope.ignoreDns = (hostname) => {
		$scope.data.dnsIgnore.push(hostname);
	};
	
	$scope.ignoreFirewall = (destIp) => {
		$scope.data.firewallIgnore.push(destIp);
	};
})
.filter('toDate', () => (epoch) => (new Date(epoch)).toLocaleString())
.filter('toTrClass', () => (destAddress) => {
	if("BLOCK" == destAddress){
		return "danger";
	}
	
	if("192.168.1.9" == destAddress){
		return "danger";
	}
	
	return "success";
});

