function MainCtrl(Navigation, WebsiteService, Storage, filesService, UserService, AboutService, $filter, $route, $routeParams, $scope, $location, $http, $sce) {

  $scope.$route = $route;
  $scope.$location = $location;
  $scope.$routeParams = $routeParams;
  
  $scope.Storage = Storage;
  
  $scope.WebsiteService = WebsiteService;
  $scope.websites = WebsiteService.websites;
  $scope.websiteNames = WebsiteService.websiteNames;
  $scope.dateKeys = WebsiteService.dateKeys;
  
  $scope.searchedWebsiteNames = WebsiteService.searchedWebsiteNames;
  $scope.searchedWebsiteDateKeys = WebsiteService.searchedWebsiteDateKeys;

  $scope.dateKey = false;
  $scope.topWebsites = false;
  $scope.topLimit = 5;

  $scope.websiteName = false;
  $scope.websiteData = false;
  $scope.websiteDataLimit = 8;
  
  $scope.reportType = "chart"; // list or chart
  
  $scope.UserService = UserService;
  $scope.login = UserService.login;
  $scope.isLoggedIn = false;
  
  $scope.filesService = filesService;
  $scope.fileUploadResponse = false;
  $scope.theFile = false;
  $scope.fileUploadForm = false;
  
  $scope.AboutService = AboutService;
  $scope.about = AboutService.about;
  
  $scope.isIe = navigator.userAgent.indexOf('MSIE') >= 0;
  
  $scope.init = function() {
    $scope.initThemes();
  };
  
  $scope.reInit = function() {
  	$scope.WebsiteService.init();
  };
  
  /* Website */
  $scope.$on('WebsiteLoaded', function(event, msg) {
    $scope.initWebsites();
  });
  $scope.$on('WebsiteNamesLoaded', function(event, msg) {
    $scope.initWebsiteNames();
  });
  $scope.$on('DateKeysLoaded', function(event, msg) {
    $scope.initWebsiteDateKeys();
  });
  $scope.$on('TopWebsitesUpdated', function(event, msg) {
    $scope.initTopWebsites();
  });
  $scope.$on('WebsiteDataUpdated', function(event, msg) {
    $scope.initWebsiteData();
  });
  $scope.$on('SearchedWebsiteNamesLoaded', function(event, msg) {
    $scope.initSearchedWebsiteNames();
  });
  $scope.$on('SearchedWebsiteDateKeysLoaded', function(event, msg) {
    $scope.initSearchedWebsiteDateKeys();
  });
  
  $scope.initWebsites = function() {
    $scope.websites = WebsiteService.websites;
  };
  $scope.initWebsiteNames = function() {
    $scope.websiteNames = WebsiteService.websiteNames;
    
    // select first name from name list and init the data
    if ($scope.websiteName === false) {
      if ($scope.websiteNames != false && $scope.websiteNames.length > 0) {
        var obj = $scope.websiteNames[0];
        if (obj.name) {
          $scope.updateWebsiteData(obj.name);
        }
      }
    }
  };
  $scope.initWebsiteDateKeys = function() {
    $scope.dateKeys = WebsiteService.dateKeys;
    
    // select first dateKey from dateKey list and init the report
    if ($scope.dateKey === false) {
      if ($scope.dateKeys != false && $scope.dateKeys.length > 0) {
        var obj = $scope.dateKeys[0];
        if (obj.dateKey) {
          $scope.updateTopWebsites(obj.dateKey);
        }
      }
    }
  };
  $scope.initSearchedWebsiteNames = function() {
    if (WebsiteService.searchedWebsiteNames == false) return;
    if ($scope.websiteNames != false) {
      var names = WebsiteService.searchedWebsiteNames
      for (var i in $scope.websiteNames) {
        var name = $scope.websiteNames[i];
        for (var j in names) {
          if (name.name === names[j].name) {
            names.splice(j, 1);
          }
        }
      }
      if (names.length > 0) {
        $scope.searchedWebsiteNames = names;
      }
    } else {
      $scope.searchedWebsiteNames = WebsiteService.searchedWebsiteNames;
    }
  };
  $scope.initSearchedWebsiteDateKeys = function() {
    if (WebsiteService.searchedWebsiteDateKeys == false) return;
    if ($scope.dateKeys != false) {
      var dateKeys = WebsiteService.searchedWebsiteDateKeys
      for (var i in $scope.dateKeys) {
        var dateKey = $scope.dateKeys[i];
        for (var j in dateKeys) {
          if (dateKey.dateKey === dateKeys[j].dateKey) {
            dateKeys.splice(j, 1);
          }
        }
      }
      if (dateKeys.length > 0) {
        $scope.searchedWebsiteDateKeys = dateKeys;
      }
    } else {
      $scope.searchedWebsiteDateKeys = WebsiteService.searchedWebsiteDateKeys;
    }
  };
  
  /* reporting */
  $scope.initReport = function() {
    var params = $route.current.params;
    if (params.dateKey) {
      $scope.updateTopWebsites(params.dateKey);
    }
    $scope.initTopWebsites();
  };
  
  $scope.initTopWebsites = function() {
    $scope.dateKey = WebsiteService.dateKey;
    $scope.topWebsites = WebsiteService.topWebsites;
    $scope.updateTopWebsiteCharts();
  };
  $scope.updateTopWebsiteCharts = function() {
    $scope.updateWebsiteCharts("#topWebsitesContainer", $scope.dateKey, $scope.topWebsites, $scope.topLimit);
  };
  $scope.resetTopWebsites = function() {
    $scope.dateKey = false;
    $scope.topWebsites = false;
  };
  $scope.updateTopWebsites = function(dateKey) {
    if (dateKey === false) return;
    WebsiteService.getTopWebsitesOn(dateKey);
  };
  $scope.updateReport = function(dateKey) {
    $location.path('/report/' + dateKey);
    $scope.initReport();
  };
  $scope.updateTopLimit = function() {
    $scope.topLimit = this.topLimit;
    $scope.updateTopWebsiteCharts();
  };
  
  /* report page */
  $scope.setReportType = function(type) {
    if (type != "list" && type != "chart") type = "list";
    $scope.reportType = type;
  };
  
  /* website data */
  $scope.initWebsitePage = function() {
    var params = $route.current.params;
    if (params.name) {
      $scope.updateWebsiteData(params.name);
    }
    $scope.initWebsiteData();
  };
  $scope.initWebsiteData = function() {
    $scope.websiteName = WebsiteService.websiteName;
    $scope.websiteData = WebsiteService.websiteData;
    $scope.updateWebsiteDataChart("#websiteDataContainer", $scope.websiteName, $scope.websiteData, $scope.websiteDataLimit);
  };
  $scope.updateWebsiteData = function(name) {
    if (name === false) return;
    WebsiteService.getWebsiteData(name);
  };
  $scope.updateWebsiteDataLimit = function() {
    $scope.websiteDataLimit = this.websiteDataLimit;
  };
  
  /* Login */
  $scope.$on('LoginChange', function(event, msg) {
    $scope.initLogin();
    
    // redirect to index page when logout
    if ($scope.login) {
      if (!$scope.login.isLoggedIn) {
        if ($location.url() == '/manage') {
          $location.url('/');
        }
      }
    }
  });
  
  $scope.initLogin = function() {
    $scope.login = UserService.login;
    if (!$scope.login) {
      $scope.isLoggedIn = false;
    } else {
      $scope.isLoggedIn = $scope.login.isLoggedIn;
    }
  };
  
  $scope.doLogin = function() {
    console.log('doLogin -->');
    if (this.username && this.password) {
      var _form = jQuery('#loginForm');
      if (_form.length > 0) {
        UserService.doLogin(_form);
      }
    }
  };
  
  $scope.doLogout = function() {
    console.log('doLogout -->');
    UserService.doLogout();
  }
  
  /* Data management*/
  $scope.deleteWebsite = function(id) {
    $scope.WebsiteService.deleteWebsite(id);
  };
  $scope.searchWebsiteName = function(name) {
    $scope.WebsiteService.searchWebsiteName(encodeURIComponent('%'+name+'%'));
  };
  $scope.searchWebsiteDateKey = function(search) {    
    var dateKey = '';
    
    if (search == false) return;
    dateKey += '%';
    if (search.year) dateKey += search.year;
    dateKey += '%-%';
    if (search.month) dateKey += search.month;
    dateKey += '%-%';
    if (search.day) dateKey += search.day;
    dateKey += '%';
    
    $scope.WebsiteService.searchWebsiteDateKey(encodeURIComponent(dateKey));
  };

  /* Files */
  $scope.$on('FilesChange', function(event, msg) {
    $scope.initFileUploadResponse();
    $scope.reInit();
  });
  
  $scope.initFileUploadResponse = function() {
    $scope.fileUploadResponse = filesService.response;
    $scope.resetUploadForm();
  };
  
  $scope.resetForm = function() {
    if (!$scope.fileUploadForm) {
      $scope.fileUploadForm = this;
      $scope.resetUploadForm();
      $scope.fileUploadForm = false;
    } else {
      $scope.resetUploadForm();
    }
  };
  
  // TODO: Resetting the upload form, below is not a good practice
  $scope.resetUploadForm = function() {
    $scope.theFile = false;
    //jQuery('#uploadForm')[0].reset();
    var _form = jQuery('#uploadForm');
    
    if (_form.length > 0) {
      _form[0].reset();
    }
    
    // somehow theFile is inside the form's scope
    if ($scope.fileUploadForm) {
      if ($scope.fileUploadForm.theFile) {
        $scope.fileUploadForm.theFile = false;
      }
    }
  };
  
  $scope.submitUploadForm = function() {
    $scope.fileUploadForm = this;
    if (this.theFile) {
      $scope.theFile = this.theFile;
      filesService.uploadFile(this.theFile);
    }
  };
  
  // alternative method to submitUploadForm
  $scope.uploadFile = function(file) {
    if (file) {
      filesService.uploadFile(file);
    }
  };
  
  $scope.deleteUploadedFile = function(filename) {
    if (filename) {
      filesService.deleteUploadedFile(filename);
    }
  };
  
  /* About */
  $scope.$on('AboutLoaded', function(event, msg) {
    $scope.initAbout();
  });
  
  $scope.initAbout = function() {
    $scope.about = AboutService.about;
  };
  
  /* Themes */
  $scope.initThemes = function() {
    $scope.themes = ["default", "amelia", "cerulean", "cosmo", "cyborg", "flatly", "journal", "readable", "simplex", "slate", "spacelab", "united"]; // zero-index
    $scope.themeIndex = 3; // set initial theme index
    if ($scope.isIe) {
      $scope.themes = _.without($scope.themes, "cerulean", "slate", "spacelab");
    }
    $scope.themeCount = $scope.themes.length;
    
    if ($scope.themeIndex >= $scope.themeCount) {
      $scope.themeIndex = 0;
    }
    if ($scope.themeCount > 0) {
      $scope.theme = $scope.themes[$scope.themeIndex];
    }
    
    // retrieve saved theme from localStorage
    var savedTheme = $scope.Storage.loadObject('theme');
    if (savedTheme != undefined && savedTheme != null && savedTheme.length > 0)
      $scope.theme = savedTheme;
  };
  
  $scope.setTheme = function(theme) {
    $scope.theme = theme;
    // save theme to localStorage
    $scope.Storage.saveObject($scope.theme, 'theme');
  };
  
  /**
   * Register below method to ng-click in order to have the theme change on a click of a button.
   */
  $scope.changeTheme = function() {
    $scope.themeIndex = $scope.themeIndex + 1;
    if ($scope.themeIndex >= $scope.themeCount) {
      $scope.themeIndex = 0;
    }
    $scope.setTheme($scope.themes[$scope.themeIndex]);
  };

  /* Misc */
  $scope.showSidebar = function() {
    $('.row-offcanvas').toggleClass('active');
  };
  $scope.getUnsafeHtml = function(html) {
    return $sce.trustAsHtml(html);
  };
  $scope.getAlertClass = function(status) {
    status = $filter('uppercase')(status);
    if (status == 'OK') return 'alert-success';
    if (status == 'FAIL') return 'alert-danger';
    return 'alert-warning';
  };
  $scope.getActive = function(path) {
    if ($location.path().substr(0, path.length) == path) return "active"
    return "";
  };
  $scope.getReportActive = function(dateKey) {
    if ($scope.dateKey == dateKey) return "active"
    return "";
  };
  $scope.getReportTypeActive = function(type) {
    if ($scope.reportType == type) return "active"
    return "";
  };
  $scope.getWebsiteNameActive = function(name) {
    if ($scope.websiteName == name) return "active"
    return "";
  };
  
  /* Charts */
  // top websites chart
  $scope.updateWebsiteCharts = function(container, dateKey, websites, limit) {
    if (dateKey === false || websites === false) return;
    if (limit == false || limit == undefined || limit == null) limit = 5;
    
    var len = websites.length;
    if (len > limit) len = limit;
    var names = [];
    var series = [];
    
    for(var i in websites) {
      var name = websites[i].name;
      var visits = websites[i].visits;
      
      var data = [];
      
      for (var j=0; j<len; j++) {
        if (j==i) data[j] = visits;
        else data[j] = null;
      }
      
      names[i] = name;
      series[i] = {"name" : name, "data" : data};
      
      if (i >= len-1) break;
    }
    
    $(function() {
      $(container)
          .highcharts(
              {
                chart : {
                  type : 'column'
                },
                title : {
                  text : 'Top '+ limit + ' Websites on ' + dateKey
                },
                xAxis : {
                  categories : names
                },
                yAxis : {
                  min : 0,
                  title : {
                    text : 'Visits'
                  }
                },
                tooltip : {
                  headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
                  pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
                      + '<td style="padding:0"><b>{point.y}</b></td></tr>',
                  footerFormat : '</table>',
                  shared : true,
                  useHTML : true
                },
                plotOptions : {
                  column : {
                    pointPadding : 0.2,
                    borderWidth : 0,
                    stacking : 'normal'
                  }
                },
                series : series
              });
    });
  };
  
  // website chart
  $scope.updateWebsiteDataChart = function(container, name, websites, limit) {
    if (name === false || websites === false) return;
    if (limit == false || limit == undefined || limit == null) limit = 5;
    
    var len = websites.length;
    if (len > limit) len = limit;    
    var dateKeys = [];
    var series = [];
    var data = [];
    
    for(var i in websites) {
      var dateKey = websites[i].dateKey;
      var visits = websites[i].visits;
      
      dateKeys[i] = dateKey;
      data[i] = visits;
      
      if (i >= len-1) break;
    }
    
    // reverse arrays
    dateKeys.reverse();
    data.reverse();
    
    series[0] = {"name" : name, "data" : data};
    
    $(function() {
      $(container)
          .highcharts(
              {
                title : {
                  text : '' + name
                },
                xAxis : {
                  categories : dateKeys
                },
                yAxis : {
                  min : 0,
                  title : {
                    text : 'Visits'
                  }
                },
                tooltip : {
                  headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
                  pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
                      + '<td style="padding:0"><b>{point.y}</b></td></tr>',
                  footerFormat : '</table>',
                  shared : true,
                  useHTML : true
                },
                plotOptions : {
                  column : {
                    pointPadding : 0.2,
                    borderWidth : 0,
                    stacking : 'normal'
                  }
                },
                series : series
              });
    });
  };
  
  $scope.print = function() {
    if (window.print) {
      window.print();
    }
  };
}