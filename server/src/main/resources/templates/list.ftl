<div class="generic-container">
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">List of Players</span></div>
		<div class="panel-body">
			
			<div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
	        <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
			<div class="table-responsive">
		        <table class="table table-hover">
		            <thead>
		            <tr>
		                <th>ID</th>
		                <th>Status</th>
		                <th>Current Number</th>
		                <th>IP</th>
		                <th>PORT</th>
		                <th>Type</th>
		                <th>LINK</th>
		                <th width="100"></th>
		                <th width="100"></th>
		            </tr>
		            </thead>
		            <tbody>
		            <tr ng-repeat="u in ctrl.getAllPlayers()">
		                <td>{{u.id}}</td>
		                <td>{{u.status}}</td>
		                <td>{{u.currentNumber}}</td>
		                <td>{{u.ip}}</td>
		                <td>{{u.port}}</td>
		                <td>{{u.type}}</td>
		                <td><a href="http://{{u.ip}}:{{u.port}}/player/" target="_blank"/>Link</a></td>
		                <td><button type="button" ng-click="ctrl.removePlayer(u.id)" class="btn btn-danger custom-width">Remove</button></td>
		                <td><button type="button" ng-click="ctrl.startGamePlayer(u.id)" class="btn btn-danger custom-width">Start</button></td>
		            </tr>
		            </tbody>
		        </table>	
		        
		         	
			</div>
		</div>
    </div>
</div>