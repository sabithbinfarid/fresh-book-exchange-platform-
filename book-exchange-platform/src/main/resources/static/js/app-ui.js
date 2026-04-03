(function () {
    const STORAGE_KEY = "bookex_theme";
    const MEDIA_QUERY = window.matchMedia("(prefers-color-scheme: dark)");

    function applyTheme(theme) {
        const root = document.documentElement;
        if (theme === "light" || theme === "dark") {
            root.setAttribute("data-theme", theme);
        } else {
            root.removeAttribute("data-theme");
        }
    }

    function getStoredTheme() {
        return localStorage.getItem(STORAGE_KEY) || "system";
    }

    function setStoredTheme(theme) {
        localStorage.setItem(STORAGE_KEY, theme);
    }

    function updateThemeButton(button) {
        if (!button) {
            return;
        }
        const current = getStoredTheme();
        button.textContent = current === "system" ? "Theme: Auto" : current === "dark" ? "Theme: Dark" : "Theme: Light";
        button.setAttribute("aria-label", "Theme mode " + (current === "system" ? "auto" : current));
        button.setAttribute("title", "Switch theme mode");
    }

    function bindThemeToggle(toggleId) {
        const button = document.getElementById(toggleId);
        if (!button) {
            return;
        }

        updateThemeButton(button);

        button.addEventListener("click", function () {
            const current = getStoredTheme();
            const next = current === "system" ? "light" : current === "light" ? "dark" : "system";
            setStoredTheme(next);
            applyTheme(next);
            updateThemeButton(button);
        });

        MEDIA_QUERY.addEventListener("change", function () {
            if (getStoredTheme() === "system") {
                applyTheme("system");
                updateThemeButton(button);
            }
        });
    }

    function loadRoleBadge(badgeId) {
        const badge = document.getElementById(badgeId);
        if (!badge) {
            return;
        }

        fetch("/api/auth/me")
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("Profile unavailable");
                }
                return response.json();
            })
            .then(function (data) {
                badge.textContent = data.isAdmin ? "ADMIN" : "USER";
                badge.classList.remove("status", "success", "warn");
                badge.classList.add("status", data.isAdmin ? "success" : "warn");
            })
            .catch(function () {
                // Keep fallback badge when profile endpoint is unavailable.
            });
    }

    function bindSearchableCollection(options) {
        if (!options || !options.inputId || !options.itemSelector) {
            return;
        }

        const input = document.getElementById(options.inputId);
        if (!input) {
            return;
        }

        const target = options.scopeSelector ? document.querySelector(options.scopeSelector) : document;
        if (!target) {
            return;
        }

        const emptyState = options.emptyStateId ? document.getElementById(options.emptyStateId) : null;

        function runFilter() {
            const query = input.value.trim().toLowerCase();
            const items = target.querySelectorAll(options.itemSelector);
            let visibleCount = 0;

            items.forEach(function (item) {
                const haystack = (item.getAttribute("data-search") || item.textContent || "").toLowerCase();
                const visible = !query || haystack.indexOf(query) !== -1;
                item.style.display = visible ? "" : "none";
                if (visible) {
                    visibleCount += 1;
                }
            });

            if (emptyState) {
                emptyState.style.display = visibleCount === 0 ? "block" : "none";
            }
        }

        input.addEventListener("input", runFilter);
        runFilter();
    }

    function init(options) {
        const config = options || {};
        applyTheme(getStoredTheme());
        if (config.themeToggleId) {
            bindThemeToggle(config.themeToggleId);
        }
        if (config.roleBadgeId) {
            loadRoleBadge(config.roleBadgeId);
        }
        if (config.search) {
            bindSearchableCollection(config.search);
        }
    }

    window.AppUI = {
        init: init,
        loadRoleBadge: loadRoleBadge,
        bindSearchableCollection: bindSearchableCollection
    };
})();
