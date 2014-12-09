//
//  Created by R2D2 on 2014/11/27.
//  Copyright (c) 2014 Polymorph. All rights reserved.
//

var AppDelegate = function() {


    var _officeViewController = null;
    this.getOfficeViewController = function() {
        if (null == _officeViewController) {
            _officeViewController = new OfficeViewController();
        }
        _officeViewController.initialize();

        return _officeViewController;
    };

    var _polymorphDoorViewController = null;
    this.getPolymorphDoorViewController = function() {
        if (null == _polymorphDoorViewController) {
            _polymorphDoorViewController = new PolymorphDoorViewController();
        }
        _polymorphDoorViewController.initialize();

        return _polymorphDoorViewController;
    };

    var _bottomDoorViewController = null;
    this.getBottomDoorViewController = function() {
        if (null == _bottomDoorViewController) {
            _bottomDoorViewController = new BottomDoorViewController();
        }
        _bottomDoorViewController.initialize();

        return _bottomDoorViewController;
    };

    this.registerEvents = function() {
        var self = this;
        // Check of browser supports touch events...
        if (document.documentElement.hasOwnProperty('ontouchstart')) {
            // ... if yes: register touch event listener to change the "selected" state of the item
            $('body').on('touchstart', 'a', function(event) {
                $(event.target).addClass('tappable-active');
            });
            $('body').on('touchend', 'a', function(event) {
                $(event.target).removeClass('tappable-active');
            });
        } else {
            // ... if not: register mouse events instead
            $('body').on('mousedown', 'a', function(event) {
                $(event.target).addClass('tappable-active');
            });
            $('body').on('mouseup', 'a', function(event) {
                $(event.target).removeClass('tappable-active');
            });
        }
        $(window).on('hashchange', $.proxy(this.route, this));
    };

    this.route = function() {
        var hash = window.location.hash;
        if (!hash || hash.match(this.officeURL)) {
            $('#ui_area').html(this.getOfficeViewController().render().el);
        } else if (hash.match(this.polymorphURL)) {
            $('#ui_area').html(this.getPolymorphDoorViewController().render().el);
        } else if (hash.match(this.bottomURL)) {
            $('#ui_area').html(this.getBottomDoorViewController().render().el);
        }
    };

    this.initialize = function() {

        var self = this;
        this.officeURL = /^#office/;
        this.polymorphURL = /^#polymorph/;
        this.bottomURL = /^#bottom/;

        self.registerEvents();

        /* displaying alert */
        //self.showAlert('Store Initialized', 'Info');

        self.route();
    };

    this.initialize();
};

var appDelegate = AppDelegate();