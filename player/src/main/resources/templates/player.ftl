<div class="generic-container">
        <div class="panel-heading"><span class="lead">Player </span></div>
		<div class="panel-body">
	        <div class="formcontainer">
	            <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
	            <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
	            <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="uname">ID</label>
	                        <div class="col-md-7">
	                            <label>{{ctrl.player.id}}</label>
	                        </div>
	                    </div>
	                </div>

	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="status">Status</label>
	                        <div class="col-md-7">
	                            <label>{{ctrl.player.status}}</label>
	                        </div>
	                    </div>
	                </div>
	
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="currentNumber">Current Number</label>
	                        <div class="col-md-7">
	                            <input type="text" ng-model="ctrl.player.currentNumber" id="currentNumber" class="form-control input-sm" required ng-pattern="ctrl.onlyNumbers"/>
	                        </div>
	                    </div>
	                </div>
	                
	                 <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="ip">IP</label>
	                        <div class="col-md-7">
	                        	<label>{{ctrl.player.ip}}</label>
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="port">Port</label>
	                        <div class="col-md-7">
	                        	<label>{{ctrl.player.port}}</label>
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="port">Autonomous</label>
	                        <div class="col-md-7">
	                        	<input type="checkbox" ng-model="ctrl.player.autonomous" id="autonomous" ng-change="ctrl.update()"/>
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
                        <button type="button" ng-click="ctrl.startGame()" class="btn btn-warning btn-sm">Ask To Start</button>
                        <button type="button" ng-click="ctrl.manualPlay()" class="btn btn-warning btn-sm" >Manual Play</button>
	                </div>
	            </form>
    	    </div>
		</div>	
    </div>
</div>