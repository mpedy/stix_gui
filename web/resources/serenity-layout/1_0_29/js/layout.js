/** 
 * PrimeFaces Serenity Layout
 */
PrimeFaces.widget.Serenity = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this._super(cfg);
        this.wrapper = $(document.body).children('.layout-wrapper');

        var $this = this;
        $(function () {
            $this._init();
        });

        if (!this.wrapper.hasClass('layout-menu-horizontal')) {
            this.restoreMenuState();
        }

        this.expandedMenuitems = this.expandedMenuitems || [];
    },

    _init: function () {
        this.contentWrapper = this.wrapper.children('.layout-main');
        this.sidebar = this.wrapper.children('.layout-sidebar');
        this.menu = this.sidebar.find('.layout-menu');
        this.menulinks = this.menu.find('a');
        this.topbar = this.contentWrapper.find('.layout-topbar');
        this.menuButton = this.topbar.children('.menu-btn');
        this.topbarMenu = this.topbar.find('> .layout-topbar-menu-wrapper > .topbar-menu');
        this.topbarItems = this.topbarMenu.children('li');
        this.topbarLinks = this.topbarMenu.find('a');
        this.topbarMenuButton = this.topbar.find('> .topbar-menu-btn');
        this.anchorButton = this.sidebar.find('> .sidebar-logo > .sidebar-anchor');
        this.nano = this.sidebar.find('.nano');
        this.isRTL = this.wrapper.hasClass('layout-rtl');
        this.bindEvents();
    },

    bindEvents: function () {
        var $this = this;

        $('.nano').nanoScroller({flash: true, isRTL: $this.isRTL});

        this.sidebar.on('mouseenter', function (e) {

            if (!$this.wrapper.hasClass('layout-sidebar-static')) {
                if ($this.hideTimeout) {
                    clearTimeout($this.hideTimeout);
                }
                $this.sidebar.addClass('layout-sidebar-active');
            }

        })
                .on('mouseleave', function (e) {

                    if (!$this.wrapper.hasClass('layout-sidebar-static')) {
                        $this.hideTimeout = setTimeout(function () {
                            $this.sidebar.removeClass('layout-sidebar-active');
                        }, $this.cfg.closeDelay);
                    }

                });


        this.menulinks.off('click').on('click', function (e) {
            var link = $(this),
                    item = link.parent(),
                    submenu = item.children('ul');
            horizontal = $this.isHorizontal() && $this.isDesktop();

            if (horizontal) {
                $this.horizontalMenuClick = true;
            }

            if (item.hasClass('active-menuitem')) {
                if (submenu.length) {
                    $this.removeMenuitem(item.attr('id'));
                    item.removeClass('active-menuitem');

                    if (horizontal) {
                        if (item.parent().is($this.jq)) {
                            $this.menuActive = false;
                        }

                        submenu.hide();
                    } else {
                        submenu.slideUp();
                    }

                    submenu.find('.ink').remove();

                    submenu.slideUp(function () {
                        $this.removeMenuitem(item.attr('id'));
                        item.removeClass('active-menuitem');
                    });
                }
            } else {
                $this.addMenuitem(item.attr('id'));

                if (horizontal) {
                    $this.deactivateItems(item.siblings());
                    item.addClass('active-menuitem');
                    $this.menuActive = true;
                    submenu.show();
                } else {
                    $this.deactivateItems(item.siblings(), true);
                    $this.activate(item);
                }
            }

            setTimeout(function () {
                $this.nano.nanoScroller();
            }, 500);

            if (!horizontal) {
                setTimeout(function () {
                    $(".nano").nanoScroller();
                }, 500);
            }

            if (submenu.length) {
                e.preventDefault();
            }
        });

        this.menu.find('> li').on('mouseenter', function (e) {
            if ($this.isHorizontal() && $this.isDesktop()) {
                var item = $(this);

                if (!item.hasClass('active-menuitem')) {
                    $this.menu.find('.active-menuitem').removeClass('active-menuitem');
                    $this.menu.find('ul:visible').hide();
                    $this.menu.find('.ink').remove();

                    if ($this.menuActive) {
                        item.addClass('active-menuitem');
                        item.children('ul').show();
                    }
                }
            }
        });

        this.topbarLinks.off('click.topbarLink').on('click.topbarLink', function (e) {
            var link = $(this),
                    item = link.parent(),
                    submenu = link.next();

            $this.topbarClick = true;

            item.siblings('.active-topmenuitem').removeClass('active-topmenuitem');

            if ($this.isMobile()) {
                item.children('ul').removeClass('fadeInDown fadeOutUp');
                item.toggleClass('active-topmenuitem');
            } else {
                if (submenu.length) {
                    if (item.hasClass('active-topmenuitem')) {
                        submenu.addClass('fadeOutUp').removeClass('fadeInDown');

                        setTimeout(function () {
                            item.removeClass('active-topmenuitem'),
                                    submenu.removeClass('fadeOutUp');
                        }, 450);
                    } else {
                        item.addClass('active-topmenuitem');
                        submenu.addClass('fadeInDown');
                    }
                }
            }

            var href = link.attr('href');
            if (href && href !== '#') {
                window.location.href = href;
            }

            e.preventDefault();
        });

        this.anchorButton.on('click', function (e) {
            $this.wrapper.removeClass('layout-wrapper-static-restore');
            $this.wrapper.toggleClass('layout-wrapper-static');
            $this.saveMenuState();
            e.preventDefault();
        });

        this.menuButton.on('click', function (e) {
            $this.wrapper.removeClass('layout-wrapper-static-restore').toggleClass('layout-wrapper-active');
            $(document.body).toggleClass('hidden-overflow');

            setTimeout(function () {
                $this.nano.nanoScroller();
            }, 500);

            e.preventDefault();
        });

        this.topbarMenuButton.on('click', function (e) {
            $this.topbarClick = true;
            $this.topbarMenu.find('ul').removeClass('fadeInDown fadeOutUp');

            if ($this.topbarMenu.hasClass('topbar-menu-active')) {
                $this.topbarMenu.addClass('fadeOutUp').removeClass('fadeInDown');

                setTimeout(function () {
                    $this.topbarMenu.removeClass('topbar-menu-active fadeOutUp');
                }, 450);
            } else {
                $this.topbarMenu.addClass('topbar-menu-active fadeInDown');
            }

            e.preventDefault();
        });

        this.topbarItems.filter('.search-item').on('click', function () {
            $this.topbarClick = true;
        });

        this.contentWrapper.children('.layout-main-mask').on('click', function (e) {
            $this.wrapper.removeClass('layout-wrapper-active layout-wrapper-static-restore');
            $(document.body).removeClass('hidden-overflow');
        });

        $(document.body).on('click', function () {
            if ($this.isHorizontal() && !$this.horizontalMenuClick && $this.isDesktop()) {
                $this.menu.find('.active-menuitem').removeClass('active-menuitem');
                $this.menu.find('ul:visible').hide();
                $this.menuActive = false;
            }

            if (!$this.topbarClick) {
                $this.topbarItems.filter('.active-topmenuitem').removeClass('active-topmenuitem');
                $this.topbarMenu.removeClass('topbar-menu-active');
            }

            $this.horizontalMenuClick = false;
            $this.topbarClick = false;
        });
    },

    activate: function (item) {
        var submenu = item.children('ul');
        item.addClass('active-menuitem');

        if (submenu.length) {
            submenu.slideDown();
        }
    },

    deactivate: function (item) {
        var submenu = item.children('ul');
        item.removeClass('active-menuitem').find('.ink').remove();

        if (submenu.length) {
            submenu.hide();
            submenu.find('.ink').remove();
        }
    },

    deactivateItems: function (items, animate) {
        var $this = this;

        for (var i = 0; i < items.length; i++) {
            var item = items.eq(i),
                    submenu = item.children('ul');

            if (submenu.length) {
                if (item.hasClass('active-menuitem')) {
                    var activeSubItems = item.find('.active-menuitem');
                    item.removeClass('active-menuitem');

                    if (animate) {
                        submenu.slideUp('normal', function () {
                            $(this).parent().find('.active-menuitem').each(function () {
                                $this.deactivate($(this));
                            });
                        });
                    } else {
                        submenu.hide();
                        item.find('.active-menuitem').each(function () {
                            $this.deactivate($(this));
                        });
                    }

                    $this.removeMenuitem(item.attr('id'));
                    activeSubItems.each(function () {
                        $this.removeMenuitem($(this).attr('id'));
                    });
                } else {
                    item.find('.active-menuitem').each(function () {
                        var subItem = $(this);
                        $this.deactivate(subItem);
                        $this.removeMenuitem(subItem.attr('id'));
                    });
                }
            } else if (item.hasClass('active-menuitem')) {
                $this.deactivate(item);
                $this.removeMenuitem(item.attr('id'));
            }
        }
    },

    removeMenuitem: function (id) {
        this.expandedMenuitems = $.grep(this.expandedMenuitems, function (value) {
            return value !== id;
        });
        this.saveMenuState();
    },

    addMenuitem: function (id) {
        if ($.inArray(id, this.expandedMenuitems) === -1) {
            this.expandedMenuitems.push(id);
        }
        this.saveMenuState();
    },

    saveMenuState: function () {
        if (this.isHorizontal()) {
            return;
        }

        if (this.wrapper.hasClass('layout-wrapper-static'))
            $.cookie('serenity_menu_static', 'serenity_menu_static', {path: '/'});
        else
            $.removeCookie('serenity_menu_static', {path: '/'});

        $.cookie('serenity_expandeditems', this.expandedMenuitems.join(','), {path: '/'});
    },

    clearMenuState: function () {
        $.removeCookie('serenity_expandeditems', {path: '/'});
        $.removeCookie('serenity_menu_static', {path: '/'});
    },

    restoreMenuState: function () {
        var menuCookie = $.cookie('serenity_expandeditems');
        if (menuCookie) {
            this.expandedMenuitems = menuCookie.split(',');
            for (var i = 0; i < this.expandedMenuitems.length; i++) {
                var id = this.expandedMenuitems[i];
                if (id) {
                    var menuitem = $("#" + this.expandedMenuitems[i].replace(/:/g, "\\:"));
                    menuitem.addClass('active-menuitem');

                    var submenu = menuitem.children('ul');
                    if (submenu.length) {
                        submenu.show();
                    }
                }
            }
        }

        var sidebarCookie = $.cookie('serenity_menu_static');
        if (sidebarCookie) {
            this.wrapper.addClass('layout-wrapper-static layout-wrapper-static-restore');
        }
    },

    hideTopBar: function () {
        var $this = this;
        this.topbarMenu.addClass('fadeOutUp');

        setTimeout(function () {
            $this.topbarMenu.removeClass('fadeOutUp topbar-menu-visible');
        }, 500);
    },

    isMobile: function () {
        return window.innerWidth <= 640;
    },

    isHorizontal: function () {
        return this.wrapper.hasClass('layout-menu-horizontal');
    },

    isDesktop: function () {
        return window.innerWidth > 1024;
    }
});

/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2006, 2014 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD (Register as an anonymous module)
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    var pluses = /\+/g;

    function encode(s) {
        return config.raw ? s : encodeURIComponent(s);
    }

    function decode(s) {
        return config.raw ? s : decodeURIComponent(s);
    }

    function stringifyCookieValue(value) {
        return encode(config.json ? JSON.stringify(value) : String(value));
    }

    function parseCookieValue(s) {
        if (s.indexOf('"') === 0) {
            // This is a quoted cookie as according to RFC2068, unescape...
            s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
        }

        try {
            // Replace server-side written pluses with spaces.
            // If we can't decode the cookie, ignore it, it's unusable.
            // If we can't parse the cookie, ignore it, it's unusable.
            s = decodeURIComponent(s.replace(pluses, ' '));
            return config.json ? JSON.parse(s) : s;
        } catch (e) {
        }
    }

    function read(s, converter) {
        var value = config.raw ? s : parseCookieValue(s);
        return $.isFunction(converter) ? converter(value) : value;
    }

    var config = $.cookie = function (key, value, options) {

        // Write

        if (arguments.length > 1 && !$.isFunction(value)) {
            options = $.extend({}, config.defaults, options);

            if (typeof options.expires === 'number') {
                var days = options.expires, t = options.expires = new Date();
                t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
            }

            return (document.cookie = [
                encode(key), '=', stringifyCookieValue(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path ? '; path=' + options.path : '',
                options.domain ? '; domain=' + options.domain : '',
                options.secure ? '; secure' : ''
            ].join(''));
        }

        // Read

        var result = key ? undefined : {},
                // To prevent the for loop in the first place assign an empty array
                // in case there are no cookies at all. Also prevents odd result when
                // calling $.cookie().
                cookies = document.cookie ? document.cookie.split('; ') : [],
                i = 0,
                l = cookies.length;

        for (; i < l; i++) {
            var parts = cookies[i].split('='),
                    name = decode(parts.shift()),
                    cookie = parts.join('=');

            if (key === name) {
                // If second argument (value) is a function it's a converter...
                result = read(cookie, value);
                break;
            }

            // Prevent storing a cookie that we couldn't decode.
            if (!key && (cookie = read(cookie)) !== undefined) {
                result[name] = cookie;
            }
        }

        return result;
    };

    config.defaults = {};

    $.removeCookie = function (key, options) {
        // Must not alter options, thus extending a fresh object...
        $.cookie(key, '', $.extend({}, options, {expires: -1}));
        return !$.cookie(key);
    };

}));

/* JS extensions to support material animations */
if (PrimeFaces.widget.InputSwitch) {
    PrimeFaces.widget.InputSwitch = PrimeFaces.widget.InputSwitch.extend({

        init: function (cfg) {
            this._super(cfg);

            if (this.input.prop('checked')) {
                this.jq.addClass('ui-inputswitch-checked');
            }
        },

        toggle: function () {
            var $this = this;

            if (this.input.prop('checked')) {
                this.uncheck();
                setTimeout(function () {
                    $this.jq.removeClass('ui-inputswitch-checked');
                }, 100);
            } else {
                this.check();
                setTimeout(function () {
                    $this.jq.addClass('ui-inputswitch-checked');
                }, 100);
            }
        }
    });
}

if (PrimeFaces.widget.SelectBooleanButton) {
    PrimeFaces.widget.SelectBooleanButton.prototype.check = function () {
        if (!this.disabled) {
            this.input.prop('checked', true);
            this.jq.addClass('ui-state-active').children('.ui-button-text').contents()[0].textContent = this.cfg.onLabel;

            if (this.icon.length > 0) {
                this.icon.removeClass(this.cfg.offIcon).addClass(this.cfg.onIcon);
            }

            this.input.trigger('change');
        }
    }

    PrimeFaces.widget.SelectBooleanButton.prototype.uncheck = function () {
        if (!this.disabled) {
            this.input.prop('checked', false);
            this.jq.removeClass('ui-state-active').children('.ui-button-text').contents()[0].textContent = this.cfg.offLabel;

            if (this.icon.length > 0) {
                this.icon.removeClass(this.cfg.onIcon).addClass(this.cfg.offIcon);
            }

            this.input.trigger('change');
        }
    }
}

PrimeFaces.skinInput = function (input) {
    setTimeout(function () {
        if (input.val() != '') {
            var parent = input.parent();
            input.addClass('ui-state-filled');

            if (parent.is("span:not('.md-inputfield')")) {
                parent.addClass('md-inputwrapper-filled');
            }
        }
    }, 1);

    input.on('mouseenter', function () {
        $(this).addClass('ui-state-hover');
    })
            .on('mouseleave', function () {
                $(this).removeClass('ui-state-hover');
            })
            .on('focus', function () {
                var parent = input.parent();
                $(this).addClass('ui-state-focus');

                if (parent.is("span:not('.md-inputfield')")) {
                    parent.addClass('md-inputwrapper-focus');
                }
            })
            .on('blur', function () {
                $(this).removeClass('ui-state-focus');

                if (input.hasClass('hasDatepicker')) {
                    setTimeout(function () {
                        PrimeFaces.onInputBlur(input);
                    }, 150);
                } else {
                    PrimeFaces.onInputBlur(input);
                }
            });

    //aria
    input.attr('role', 'textbox')
            .attr('aria-disabled', input.is(':disabled'))
            .attr('aria-readonly', input.prop('readonly'));

    if (input.is('textarea')) {
        input.attr('aria-multiline', true);
    }

    return this;
};

PrimeFaces.onInputBlur = function (input) {
    var parent = input.parent(),
            hasInputFieldClass = parent.is("span:not('.md-inputfield')");

    if (parent.hasClass('md-inputwrapper-focus')) {
        parent.removeClass('md-inputwrapper-focus');
    }

    if (input.val() != '') {
        input.addClass('ui-state-filled');
        if (hasInputFieldClass) {
            parent.addClass('md-inputwrapper-filled');
        }
    } else {
        input.removeClass('ui-state-filled');
        parent.removeClass('md-inputwrapper-filled');
    }
};

if (PrimeFaces.widget.AutoComplete) {
    PrimeFaces.widget.AutoComplete.prototype.setupMultipleMode = function () {
        var $this = this;
        this.multiItemContainer = this.jq.children('ul');
        this.inputContainer = this.multiItemContainer.children('.ui-autocomplete-input-token');

        this.multiItemContainer.hover(function () {
            $(this).addClass('ui-state-hover');
        },
                function () {
                    $(this).removeClass('ui-state-hover');
                }
        ).click(function () {
            $this.input.focus();
        });

        //delegate events to container
        this.input.focus(function () {
            $this.multiItemContainer.addClass('ui-state-focus');
            $this.jq.addClass('md-inputwrapper-focus');
        }).blur(function (e) {
            $this.multiItemContainer.removeClass('ui-state-focus');
            $this.jq.removeClass('md-inputwrapper-focus').addClass('md-inputwrapper-filled');

            setTimeout(function () {
                if ($this.hinput.children().length == 0 && !$this.multiItemContainer.hasClass('ui-state-focus')) {
                    $this.jq.removeClass('md-inputwrapper-filled');
                }
            }, 150);
        });

        var closeSelector = '> li.ui-autocomplete-token > .ui-autocomplete-token-icon';
        this.multiItemContainer.off('click', closeSelector).on('click', closeSelector, null, function (event) {
            if ($this.multiItemContainer.children('li.ui-autocomplete-token').length === $this.cfg.selectLimit) {
                if (PrimeFaces.isIE(8)) {
                    $this.input.val('');
                }
                $this.input.css('display', 'inline');
                $this.enableDropdown();
            }
            $this.removeItem(event, $(this).parent());
        });
    };
}
;

if (PrimeFaces.widget.Calendar) {
    PrimeFaces.widget.Calendar.prototype.bindDateSelectListener = function () {
        var _self = this;

        this.cfg.onSelect = function () {
            if (_self.cfg.popup) {
                _self.fireDateSelectEvent();
            } else {
                var newDate = $.datepicker.formatDate(_self.cfg.dateFormat, _self.getDate());

                _self.input.val(newDate);
                _self.fireDateSelectEvent();
            }

            if (_self.input.val() != '') {
                var parent = _self.input.parent();
                parent.addClass('md-inputwrapper-filled');
                _self.input.addClass('ui-state-filled');
            }
        };
    };
}

/* Issue #924 is fixed for 5.3+ and 6.0. (compatibility with 5.3) */
if (window['PrimeFaces'] && window['PrimeFaces'].widget.Dialog) {
    PrimeFaces.widget.Dialog = PrimeFaces.widget.Dialog.extend({

        enableModality: function () {
            this._super();
            $(document.body).children(this.jqId + '_modal').addClass('ui-dialog-mask');
        },

        syncWindowResize: function () {}
    });
}

/* Issue #2131 */
if (window['PrimeFaces'] && window['PrimeFaces'].widget.Schedule) {
    PrimeFaces.widget.Schedule = PrimeFaces.widget.Schedule.extend({

        setupEventSource: function () {
            var $this = this,
                    offset = moment().utcOffset() * 60000;

            this.cfg.events = function (start, end, timezone, callback) {
                var options = {
                    source: $this.id,
                    process: $this.id,
                    update: $this.id,
                    formId: $this.cfg.formId,
                    params: [
                        {name: $this.id + '_start', value: start.valueOf() + offset},
                        {name: $this.id + '_end', value: end.valueOf() + offset}
                    ],
                    onsuccess: function (responseXML, status, xhr) {
                        PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                            widget: $this,
                            handle: function (content) {
                                callback($.parseJSON(content).events);
                            }
                        });

                        return true;
                    }
                };

                PrimeFaces.ajax.Request.handle(options);
            }
        }
    });
}


PrimeFaces.locales ['it'] = {
    closeText: 'Chiudi',
    prevText: 'Precedente',
    nextText: 'Prossimo',
    monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'],
    monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Nov', 'Dic'],
    dayNames: ['Domenica', 'Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato'],
    dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
    dayNamesMin: ['D', 'L', 'M', 'M ', 'G', 'V ', 'S'],
    weekHeader: 'Sett',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Solo Tempo',
    timeText: 'Orario',
    hourText: 'Ore',
    minuteText: 'Minuti',
    secondText: 'Secondi',
    currentText: 'Oggi',
    ampm: false,
    month: 'Mese',
    week: 'Settimana',
    day: 'Giorno',
    allDayText: 'Tutto il Giorno'
};


PrimeFaces.locales ['en_US'] = {
    closeText: 'Close',
    prevText: 'Previous',
    nextText: 'Next',
    monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ],
    monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ],
    dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
    dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Tue', 'Fri', 'Sat'],
    dayNamesMin: ['S', 'M', 'T', 'W ', 'T', 'F ', 'S'],
    weekHeader: 'Week',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix:'',
    timeOnlyTitle: 'Only Time',
    timeText: 'Time',
    hourText: 'Time',
    minuteText: 'Minute',
    secondText: 'Second',
    currentText: 'Current Date',
    ampm: false,
    month: 'Month',
    week: 'week',
    day: 'Day',
    allDayText: 'All Day',
    aria: {
        'paginator.PAGE': 'Page {0}',
        'calendar.BUTTON': 'Show Calendar',
        'datatable.sort.ASC': 'activate to sort column ascending',
        'datatable.sort.DESC': 'activate to sort column descending',
        'columntoggler.CLOSE': 'Close'
    },
    messages: {  //optional for Client Side Validation
        'javax.faces.component.UIInput.REQUIRED': '{0}: Validation Error: Value is required.',
        'javax.faces.converter.IntegerConverter.INTEGER': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.IntegerConverter.INTEGER_detail': '{2}: \'{0}\' must be a number between -2147483648 and 2147483647 Example: {1}',
        'javax.faces.converter.DoubleConverter.DOUBLE': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.DoubleConverter.DOUBLE_detail': '{2}: \'{0}\' must be a number between 4.9E-324 and 1.7976931348623157E308  Example: {1}',
        'javax.faces.converter.BigDecimalConverter.DECIMAL': '{2}: \'{0}\' must be a signed decimal number.',
        'javax.faces.converter.BigDecimalConverter.DECIMAL_detail': '{2}: \'{0}\' must be a signed decimal number consisting of zero or more digits, that may be followed by a decimal point and fraction.  Example: {1}',
        'javax.faces.converter.BigIntegerConverter.BIGINTEGER': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.BigIntegerConverter.BIGINTEGER_detail': '{2}: \'{0}\' must be a number consisting of one or more digits. Example: {1}',
        'javax.faces.converter.ByteConverter.BYTE': '{2}: \'{0}\' must be a number between 0 and 255.',
        'javax.faces.converter.ByteConverter.BYTE_detail': '{2}: \'{0}\' must be a number between 0 and 255.  Example: {1}',
        'javax.faces.converter.CharacterConverter.CHARACTER': '{1}: \'{0}\' must be a valid character.',
        'javax.faces.converter.CharacterConverter.CHARACTER_detail': '{1}: \'{0}\' must be a valid ASCII character.',
        'javax.faces.converter.ShortConverter.SHORT': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.ShortConverter.SHORT_detail': '{2}: \'{0}\' must be a number between -32768 and 32767 Example: {1}',
        'javax.faces.converter.BooleanConverter.BOOLEAN': '{1}: \'{0}\' must be \'true\' or \'false\'',
        'javax.faces.converter.BooleanConverter.BOOLEAN_detail': '{1}: \'{0}\' must be \'true\' or \'false\'.  Any value other than \'true\' will evaluate to \'false\'.',
        'javax.faces.validator.LongRangeValidator.MAXIMUM': '{1}: Validation Error: Value is greater than allowable maximum of \'{0}\'',
        'javax.faces.validator.LongRangeValidator.MINIMUM': '{1}: Validation Error: Value is less than allowable minimum of \'{0}\'',
        'javax.faces.validator.LongRangeValidator.NOT_IN_RANGE': '{2}: Validation Error: Specified attribute is not between the expected values of {0} and {1}.',
        'javax.faces.validator.LongRangeValidator.TYPE={0}': 'Validation Error: Value is not of the correct type.',
        'javax.faces.validator.DoubleRangeValidator.MAXIMUM': '{1}: Validation Error: Value is greater than allowable maximum of \'{0}\'',
        'javax.faces.validator.DoubleRangeValidator.MINIMUM': '{1}: Validation Error: Value is less than allowable minimum of \'{0}\'',
        'javax.faces.validator.DoubleRangeValidator.NOT_IN_RANGE': '{2}: Validation Error: Specified attribute is not between the expected values of {0} and {1}',
        'javax.faces.validator.DoubleRangeValidator.TYPE={0}': 'Validation Error: Value is not of the correct type',
        'javax.faces.converter.FloatConverter.FLOAT': '{2}: \'{0}\' must be a number consisting of one or more digits.',
        'javax.faces.converter.FloatConverter.FLOAT_detail': '{2}: \'{0}\' must be a number between 1.4E-45 and 3.4028235E38  Example: {1}',
        'javax.faces.converter.DateTimeConverter.DATE': '{2}: \'{0}\' could not be understood as a date.',
        'javax.faces.converter.DateTimeConverter.DATE_detail': '{2}: \'{0}\' could not be understood as a date. Example: {1}',
        'javax.faces.converter.DateTimeConverter.TIME': '{2}: \'{0}\' could not be understood as a time.',
        'javax.faces.converter.DateTimeConverter.TIME_detail': '{2}: \'{0}\' could not be understood as a time. Example: {1}',
        'javax.faces.converter.DateTimeConverter.DATETIME': '{2}: \'{0}\' could not be understood as a date and time.',
        'javax.faces.converter.DateTimeConverter.DATETIME_detail': '{2}: \'{0}\' could not be understood as a date and time. Example: {1}',
        'javax.faces.converter.DateTimeConverter.PATTERN_TYPE': '{1}: A \'pattern\' or \'type\' attribute must be specified to convert the value \'{0}\'', 
        'javax.faces.converter.NumberConverter.CURRENCY': '{2}: \'{0}\' could not be understood as a currency value.',
        'javax.faces.converter.NumberConverter.CURRENCY_detail': '{2}: \'{0}\' could not be understood as a currency value. Example: {1}',
        'javax.faces.converter.NumberConverter.PERCENT': '{2}: \'{0}\' could not be understood as a percentage.',
        'javax.faces.converter.NumberConverter.PERCENT_detail': '{2}: \'{0}\' could not be understood as a percentage. Example: {1}',
        'javax.faces.converter.NumberConverter.NUMBER': '{2}: \'{0}\' could not be understood as a date.',
        'javax.faces.converter.NumberConverter.NUMBER_detail': '{2}: \'{0}\' is not a number. Example: {1}',
        'javax.faces.converter.NumberConverter.PATTERN': '{2}: \'{0}\' is not a number pattern.',
        'javax.faces.converter.NumberConverter.PATTERN_detail': '{2}: \'{0}\' is not a number pattern. Example: {1}',
        'javax.faces.validator.LengthValidator.MINIMUM': '{1}: Validation Error: Length is less than allowable minimum of \'{0}\'',
        'javax.faces.validator.LengthValidator.MAXIMUM': '{1}: Validation Error: Length is greater than allowable maximum of \'{0}\'',
        'javax.faces.validator.RegexValidator.PATTERN_NOT_SET': 'Regex pattern must be set.',
        'javax.faces.validator.RegexValidator.PATTERN_NOT_SET_detail': 'Regex pattern must be set to non-empty value.',
        'javax.faces.validator.RegexValidator.NOT_MATCHED': 'Regex Pattern not matched',
        'javax.faces.validator.RegexValidator.NOT_MATCHED_detail': 'Regex pattern of \'{0}\' not matched',
        'javax.faces.validator.RegexValidator.MATCH_EXCEPTION': 'Error in regular expression.',
        'javax.faces.validator.RegexValidator.MATCH_EXCEPTION_detail': 'Error in regular expression, \'{0}\'',
        //optional for bean validation integration in client side validation
        'javax.faces.validator.BeanValidator.MESSAGE': '{0}',
        'javax.validation.constraints.AssertFalse.message': 'must be false',
        'javax.validation.constraints.AssertTrue.message': 'must be true',
        'javax.validation.constraints.DecimalMax.message': 'must be less than or equal to {0}',
        'javax.validation.constraints.DecimalMin.message': 'must be greater than or equal to {0}',
        'javax.validation.constraints.Digits.message': 'numeric value out of bounds (<{0} digits>.<{1} digits> expected)',
        'javax.validation.constraints.Future.message': 'must be in the future',
        'javax.validation.constraints.Max.message': 'must be less than or equal to {0}',
        'javax.validation.constraints.Min.message': 'must be greater than or equal to {0}',
        'javax.validation.constraints.NotNull.message': 'may not be null',
        'javax.validation.constraints.Null.message': 'must be null',
        'javax.validation.constraints.Past.message': 'must be in the past',
        'javax.validation.constraints.Pattern.message': 'must match "{0}"',
        'javax.validation.constraints.Size.message': 'size must be between {0} and {1}'
    }
};

// English
PrimeFaces.locales ['en'] = PrimeFaces.locales ['en_US'];


//<![CDATA[
//if (PrimeFaces.widget.DataTable) {
//    //@Override
//    PrimeFaces.widget.DataTable.prototype.setupStickyHeader = function () {
//        var table = this.thead.parent(),
//                offset = table.offset(),
//                win = $(window),
//                $this = this,
//                stickyNS = 'scroll.' + this.id,
//                resizeNS = 'resize.sticky-' + this.id,
//                orginTableContent = this.jq.find('> .ui-datatable-tablewrapper > table');
//
//        /*******************************************************************************************************/
//        var layoutTopbar = $('.layout-topbar'), // Please find fixed header element
//                layoutHeaderHeight = layoutTopbar.length ? layoutTopbar.outerHeight() : 64; // added the height of layout header. (60px according to my example)
//        /*******************************************************************************************************/
//
//        this.stickyContainer = $('<div class="ui-datatable ui-datatable-sticky ui-widget"><table></table></div>');
//        this.clone = this.thead.clone(false);
//        this.stickyContainer.children('table').append(this.thead);
//        table.prepend(this.clone);
//
//        $this.stickyContainer.hide();
//        setTimeout(function () {
//            $this.stickyContainer.css({
//                position: 'absolute',
//                width: table.outerWidth(),
//                top: offset.top,
//                left: offset.left,
//                'z-index': ++PrimeFaces.zindex
//            });
//            $this.stickyContainer.show();
//        }, 350);
//
//        this.jq.prepend(this.stickyContainer);
//
//        if (this.cfg.resizableColumns) {
//            this.relativeHeight = 0;
//        }
//
//        win.off(stickyNS).on(stickyNS, function () {
//            var scrollTop = win.scrollTop(),
//                    tableOffset = table.offset();
//
//            if (scrollTop + layoutHeaderHeight > tableOffset.top) {
//                $this.stickyContainer.css({
//                    'position': 'fixed',
//                    'top': layoutHeaderHeight
//                })
//                        .addClass('ui-shadow ui-sticky');
//
//                if ($this.cfg.resizableColumns) {
//                    $this.relativeHeight = (scrollTop + layoutHeaderHeight) - tableOffset.top;
//                }
//
//                if (scrollTop + layoutHeaderHeight >= (tableOffset.top + $this.tbody.height()))
//                    $this.stickyContainer.hide();
//                else
//                    $this.stickyContainer.show();
//            } else {
//                $this.stickyContainer.css({
//                    'position': 'absolute',
//                    'top': tableOffset.top
//                })
//                        .removeClass('ui-shadow ui-sticky');
//
//                if ($this.stickyContainer.is(':hidden')) {
//                    $this.stickyContainer.show();
//                }
//
//                if ($this.cfg.resizableColumns) {
//                    $this.relativeHeight = 0;
//                }
//            }
//        })
//                .off(resizeNS).on(resizeNS, function () {
//            $this.stickyContainer.hide();
//            setTimeout(function () {
//                $this.stickyContainer.css('left', orginTableContent.offset().left);
//                $this.stickyContainer.width(table.outerWidth());
//                $this.stickyContainer.show();
//            }, 350);
//        });
//
//        //filter support
//        this.clone.find('.ui-column-filter').prop('disabled', true);
//    };
//}
//]]>